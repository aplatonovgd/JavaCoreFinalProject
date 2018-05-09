package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.*;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.CartModel;
import com.litmos.gridu.javacore.aplatonov.Models.ItemModel;
import com.litmos.gridu.javacore.aplatonov.Models.ProductModel;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

public abstract class AbstractCartRequestProcessor extends AbstractPostRequestProcessor {

    protected CartInfo cartInfo;
    protected   ProductInfo productInfo;
    protected LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo;

    protected AbstractCartRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, CartInfo cartInfo, ProductInfo productInfo,
                                           LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo) throws IOException {
        super(request, dbProcessor);
        this.cartInfo = cartInfo;
        this.productInfo = productInfo;
        this.loggedInUserInfo = loggedInUserInfo;
    }


    public CartModel getCartModel(String userId) throws SessionNotFoundException {
        try {
            return cartInfo.getCartByUserId(userId);
        }
        catch (CartNotFoundException e) {
            return createCartModel();
        }
    }

    protected CartModel createCartModel(){
        List<ItemModel> itemModelList = new ArrayList<>();

        return new CartModel(itemModelList,String.valueOf(RequestHelper.getCreationTimeMillis()));
    }

    protected void  removeCartItemFromCart(CartModel cartModel, String cardItemId) throws ItemNotfoundException, IncorrectQuantityException {
        String quantity = cartModel.getCartItemQuantity(cardItemId);
        String productId = cartModel.getProductIdByCartItemId(cardItemId);

        cartModel.removeCartItemByCartItemId(cardItemId);

        productInfo.addProductQuantity(productId,quantity);
    }

    protected void removeItemsListFromCart(List<ItemModel> cartItems, CartModel cartModel) throws IncorrectQuantityException, ItemNotfoundException {
        for(ItemModel item : cartItems){
            withdrawProductFromProductList(cartModel,item.getCartItemId());
        }
    }


    public void withdrawProductFromProductList(CartModel cartModel, String cardItemId) throws ItemNotfoundException, IncorrectQuantityException {
        String quantity = cartModel.getCartItemQuantity(cardItemId);
        String productId = cartModel.getProductIdByCartItemId(cardItemId);

        productInfo.withdrawProductFromProductDatabaseQuantity(productId,quantity);
    }



    public static class ProductInfo {

         private final Object productInfoKey = new Object();

         private List<ProductModel> productModelList;

         public ProductInfo(List<ProductModel> productModel){
            this.productModelList = productModel;
         }

         public List<ProductModel> getProductModelList() {
            return productModelList;
         }

         protected void updateProductInfo(List<ProductModel> productModelList) {
             synchronized (productInfoKey){
                this.productModelList = productModelList;
            }
         }


         protected ItemModel getItemFromProductInfo(String productId) throws ItemNotfoundException {
             synchronized (productInfoKey) {
                 Optional<ProductModel> productModelOptional = productModelList.stream().filter(arg -> arg.getId().equals(productId)).findFirst();
                 ItemModel itemModel;

                 if (productModelOptional.isPresent()) {
                     ProductModel productModel = productModelOptional.get();
                     return new ItemModel(productModel.getId(), productModel.getTitle(), productModel.getQuantity(), productModel.getPrice());
                 } else {
                     throw new ItemNotfoundException("Product not found in product list");
                 }
             }
         }

        protected void withdrawProductFromProductDatabaseQuantity(String productId, String quantity) throws ItemNotfoundException, IncorrectQuantityException {
             synchronized (productInfoKey) {
                 Optional<ProductModel> productModel = productModelList.stream().filter(arg -> arg.getId().equals(productId)).findFirst();
                 if (productModel.isPresent()) {
                     String productListQuantityOriginal = productModel.get().getAvailableInDataBase();

                     int ExpectedQuantity = Integer.parseInt(productListQuantityOriginal) - Integer.parseInt(quantity);
                     if (ExpectedQuantity < 0) {
                         throw new IncorrectQuantityException("Products quantity can't be lower the zero");
                     } else {
                         String newProductListQuantity = String.valueOf(ExpectedQuantity);
                         productModel.get().setDatabaseQuantity(newProductListQuantity);
                     }

                 } else {
                     throw new ItemNotfoundException("Product not found in product list");
                 }
             }
        }


        protected void addProductQuantity(String productId, String quantity) throws ItemNotfoundException, IncorrectQuantityException {
             synchronized (productInfoKey) {
                 Optional<ProductModel> productModel = productModelList.stream().filter(arg -> arg.getId().equals(productId)).findFirst();
                 if (productModel.isPresent()) {

                     int databaseQuantity = Integer.valueOf(productModel.get().getAvailableInDataBase());
                     String productListQuantityOriginal = productModel.get().getQuantity();
                     int ExpectedQuantity = Integer.parseInt(productListQuantityOriginal) + Integer.parseInt(quantity);

                     if (ExpectedQuantity > databaseQuantity) {
                         throw new IncorrectQuantityException("Not enough items in the database");
                     } else {

                         productModel.get().setQuantity(String.valueOf(ExpectedQuantity));
                     }

                 } else {
                     throw new ItemNotfoundException("Product not found in product list");
                 }
             }
        }


        protected void withdrawProductQuantity(String productId, String quantity) throws ItemNotfoundException, IncorrectQuantityException {
             synchronized (productInfoKey) {
                 Optional<ProductModel> productModel = productModelList.stream().filter(arg -> arg.getId().equals(productId)).findFirst();
                 if (productModel.isPresent()) {

                     String productListQuantityOriginal = productModel.get().getQuantity();

                     int ExpectedQuantity = Integer.parseInt(productListQuantityOriginal) - Integer.parseInt(quantity);
                     if (ExpectedQuantity < 0) {
                         throw new IncorrectQuantityException("Not enough items in the database");
                     } else {
                         String newProductListQuantity = String.valueOf(ExpectedQuantity);
                         productModel.get().setQuantity(newProductListQuantity);
                     }

                 } else {
                     throw new ItemNotfoundException("Product not found in product list");
                 }
             }
        }

    }

    public static class CartInfo {

        private final Object cartInfoKey = new Object();

        public Map<String, CartModel> getUserIdToCartMap() {
            synchronized (cartInfoKey) {
                return userIdToCartMap;
            }
        }

        protected Map<String, CartModel> userIdToCartMap = new HashMap<>();

        public CartModel getCartByUserId(String userId) throws CartNotFoundException {
            synchronized (cartInfoKey) {
                CartModel cartModel;
                if (userIdToCartMap.containsKey(userId)) {
                    cartModel = userIdToCartMap.get(userId);
                } else {
                    throw new CartNotFoundException("Cart not found");
                }
                return cartModel;
            }
        }

        protected void addToCartInfo(String userId, CartModel cartModel) {

            synchronized (cartInfoKey) {
                userIdToCartMap.put(userId, cartModel);
            }
        }


        protected void replaceCartInfo(String userId, CartModel cartModel){
            synchronized (cartInfoKey) {
                userIdToCartMap.replace(userId, cartModel);
            }
        }

        protected void removeCart(String userId){
            synchronized (cartInfoKey) {
                userIdToCartMap.remove(userId);
            }
        }

    }
}

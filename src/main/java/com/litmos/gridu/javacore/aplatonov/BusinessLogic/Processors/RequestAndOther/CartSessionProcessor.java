package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.IncorrectQuantityException;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.ItemNotfoundException;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.CartModel;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Models.ItemModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CartSessionProcessor extends AbstractCartRequestProcessor implements Runnable {

    private long sessionExpirationTime;
    private long sessionExpirationCheckInterval;
    private ServletContext servletContext;

    public CartSessionProcessor(HttpServletRequest request, DBProcessor dbProcessor, CartInfo cartInfo, ProductInfo productInfo,
                                LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo,
                                long sessionExpirationTime, long sessionExpirationCheckInterval, ServletContext servletContext) throws IOException {
        super(request, dbProcessor, cartInfo, productInfo, loggedInUserInfo);

        this.sessionExpirationCheckInterval = sessionExpirationCheckInterval;
        this.sessionExpirationTime = sessionExpirationTime;
        this.servletContext = servletContext;
    }


    @Override
    protected Object parseJson(String json)  {
        return null;
    }


    public void removeItemsListFromCart(List<ItemModel> itemModels, CartModel cartModel) throws IncorrectQuantityException, ItemNotfoundException {

        Iterator<ItemModel> itemModelIterator = itemModels.iterator();

        while (itemModelIterator.hasNext())
        {
            ItemModel item = itemModelIterator.next();
            servletContext.log("CSC: Trying to remove productId" + item.getProductId());
            productInfo.addProductQuantity(item.getProductId(),item.getQuantity());
            itemModelIterator.remove();
        }
    }

    @Override
    public void run() {
        servletContext.log("Cart session checker(CSC) started ");

        while (true){
            Map<String,CartModel> userIdToCartMap = cartInfo.getUserIdToCartMap();

            // check if card session is expired.
            Iterator<Map.Entry<String,CartModel>> cartModelIterator =
                    userIdToCartMap.entrySet().iterator();

            while (cartModelIterator.hasNext()){
                Map.Entry<String,CartModel> cartToSessionIdEntry = cartModelIterator.next();
                long currentTime = System.currentTimeMillis();
                long cartSessionTime = currentTime - Long.valueOf(cartToSessionIdEntry.getValue().getCartCreatedTime());
                if (cartSessionTime > sessionExpirationTime){
                    servletContext.log("CSC: found cart with expired session. UserId - " + cartToSessionIdEntry.getKey());
                    CartModel cartModel = cartToSessionIdEntry.getValue();
                    List<ItemModel> itemList= cartModel.getCartItems();

                    try {
                        /*1. Add all products to ProductList */
                        servletContext.log("CSC: Trying to remove all products from cart");
                        removeItemsListFromCart(itemList, cartModel);
                        servletContext.log("CSC: Removed successfully");


                        servletContext.log("CSC: Trying to remove cart");
                        /*2. remove entrySet from map */
                        cartModelIterator.remove();
                        servletContext.log("CSC: Cart has been successfully removed");

                    } catch (Exception e) {
                        servletContext.log("CSC: Can't remove. " + e.getMessage());
                        StringWriter outError = new StringWriter();
                        e.printStackTrace(new PrintWriter(outError));
                        servletContext.log(outError.toString());
                    }
                }
            }

            try {
                Thread.sleep(sessionExpirationCheckInterval);
            } catch (InterruptedException e) {
            }
        }
    }
}

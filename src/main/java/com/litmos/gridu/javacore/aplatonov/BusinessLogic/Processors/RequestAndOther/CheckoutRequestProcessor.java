package com.litmos.gridu.javacore.aplatonov.BusinessLogic.Processors.RequestAndOther;

import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Exceptions.*;
import com.litmos.gridu.javacore.aplatonov.BusinessLogic.Helpers.RequestHelper;
import com.litmos.gridu.javacore.aplatonov.Database.DBProcessor;
import com.litmos.gridu.javacore.aplatonov.Models.CartModel;
import com.litmos.gridu.javacore.aplatonov.Models.ItemModel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CheckoutRequestProcessor extends AbstractCartRequestProcessor {


    public CheckoutRequestProcessor(HttpServletRequest request, DBProcessor dbProcessor, CartInfo cartInfo, ProductInfo productInfo, LoginRequestProcessor.LoggedInUserInfo loggedInUserInfo) throws IOException {
        super(request, dbProcessor, cartInfo, productInfo, loggedInUserInfo);
    }

    public String processRequest() throws SessionNotFoundException, IncorrectQuantityException, IOException, SQLException, EmptyCartException, ItemNotfoundException {

        String userId = RequestHelper.getUserIdByCookies(request.getCookies(),loggedInUserInfo);

        CartModel cartModel = getCartModel(userId);
        List<ItemModel> cartItems = cartModel.getCartItems();

        return checkOutCart(cartItems, cartModel, userId);
    }

    protected String checkOutCart(List<ItemModel> cartItems, CartModel cartModel, String userId) throws IncorrectQuantityException, ItemNotfoundException, IOException, SQLException, EmptyCartException {

        if (cartItems.size() == 0) {
            throw new EmptyCartException("The cart is empty ");
        }

        /*1. remove item from ProductInfo */
        checkOutItemsProductInfo(cartItems, cartModel);

        /*2. Update Products table */

        dbProcessor.updateProductsTable(cartItems);

        /*3. Create Order */

        String orderId = createOrder(cartItems, userId, cartModel.getTotal());
        /*4. Delete Cart from Cart List */
        cartInfo.removeCart(userId);

        return orderId;
    }

    public String createOrder (List<ItemModel> cartItems, String userId, String total) throws IOException, SQLException {

        String orderId = RequestHelper.generateSessionId();
        String orderDate = RequestHelper.getCurrentDateTimeSimpleFormat();

        dbProcessor.createOrder(cartItems, orderId, userId, orderDate);

        return orderId;
    }


    protected void checkOutItemsProductInfo(List<ItemModel> cartItems, CartModel cartModel) throws IncorrectQuantityException, ItemNotfoundException {
        for(ItemModel item : cartItems){
            withdrawProductFromProductList(cartModel,item.getCartItemId());
        }
    }


    @Override
    protected Object parseJson(String json) throws InvalidJsonException {
        return null;
    }
}

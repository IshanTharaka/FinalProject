package com.seekercloud.pos.bo.custom.impl;

import com.seekercloud.pos.bo.custom.ProductBo;
import com.seekercloud.pos.dao.DaoFactory;
import com.seekercloud.pos.dao.DaoTypes;
import com.seekercloud.pos.dao.custom.ProductDao;
import com.seekercloud.pos.dto.ProductDto;
import com.seekercloud.pos.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductBoImpl implements ProductBo {

    private ProductDao dao = DaoFactory.getInstance().getDao(DaoTypes.PRODUCT);
    @Override
    public boolean saveProduct(ProductDto dto) throws SQLException, ClassNotFoundException {
        return dao.save(
                new Product(dto.getCode(), dto.getDescription(), dto.getUnitPrice(), dto.getQtyOnHand())
        );
    }

    @Override
    public boolean updateProduct(ProductDto dto) throws SQLException, ClassNotFoundException {
        return dao.update(
                new Product(dto.getCode(), dto.getDescription(), dto.getUnitPrice(), dto.getQtyOnHand())
        );
    }

    @Override
    public boolean deleteProduct(String id) throws SQLException, ClassNotFoundException {
        return dao.delete(id);
    }

    @Override
    public ArrayList<ProductDto> searchProducts(String searchText) throws SQLException, ClassNotFoundException {
        ArrayList<Product> entities = dao.searchProduct(searchText);
        ArrayList<ProductDto> dtoList = new ArrayList<>();

        for (Product p:
             entities) {
            dtoList.add(new ProductDto(p.getCode(),p.getDescription(),p.getUnitPrice(),p.getQtyOnHand()));
        }
        return dtoList;
    }

    @Override
    public ResultSet getLastID() throws SQLException, ClassNotFoundException {
        return dao.getLastID();
    }

    @Override
    public ArrayList<Product> getProductDetails(String id) throws SQLException, ClassNotFoundException {
        return dao.getProductDetails(id);
    }

    @Override
    public ArrayList<String> getProductIDs() throws SQLException, ClassNotFoundException {
        return dao.getProductIDs();
    }
}

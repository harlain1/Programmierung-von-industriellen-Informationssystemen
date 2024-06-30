package com.mycompany.like_hero_to_zero;

import com.mycompany.like_hero_to_zero.util.JsfUtil;
import com.mycompany.like_hero_to_zero.util.PaginationHelper;

import java.io.Serializable;
import java.util.ResourceBundle;
import jakarta.annotation.Resource;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.model.DataModel;
import jakarta.faces.model.ListDataModel;
import jakarta.faces.model.SelectItem;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.transaction.UserTransaction;

@Named("co2EmissionsController")
@SessionScoped
public class Co2EmissionsController implements Serializable {

    @Resource
    private UserTransaction utx = null;
    @PersistenceUnit(unitName = "my_persistence_unit")
    private EntityManagerFactory emf = null;

    private Co2Emissions current;
    private DataModel items = null;
    private Co2EmissionsJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private String adminPassword;
    private static final String ADMIN_PASSWORD = "myAdminPassword";

    
    public Co2EmissionsController() {
    }

    public Co2Emissions getSelected() {
        if (current == null) {
            current = new Co2Emissions();
            selectedItemIndex = -1;
        }
        return current;
    }

    private Co2EmissionsJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new Co2EmissionsJpaController(utx, emf);
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getJpaController().getCo2EmissionsCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findCo2EmissionsEntities(getPageSize(), getPageFirstItem()));
                }
            };
        }
        return pagination;
    }
    
     public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    private boolean isAdminPasswordCorrect() {
        return ADMIN_PASSWORD.equals(adminPassword);
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }
    
    public String prepareListHome() {
        recreateModel();
        return "Home";
    }

    public String prepareView() {
        current = (Co2Emissions) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }
    
    public String prepareViewDetail() {
        current = (Co2Emissions) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "ViewDetail";
    }

    public String prepareCreate() {
        current = new Co2Emissions();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Co2EmissionsCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Co2Emissions) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        if (!isAdminPasswordCorrect()) {
            JsfUtil.addErrorMessage("Invalid admin password.");
            return null;
        }
        try {
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Co2EmissionsUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String prepareDestroy() {
        current = (Co2Emissions) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "ConfirmDestroy";
    }
    
    public String prepareDestroyView() {
        return "ConfirmDestroy";
    }

    public String destroy() {
        if (!isAdminPasswordCorrect()) {
            JsfUtil.addErrorMessage("Invalid admin password.");
            return null;
        }
        current = (Co2Emissions) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        if (!isAdminPasswordCorrect()) {
            JsfUtil.addErrorMessage("Invalid admin password.");
            return null;
        }
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "List";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getJpaController().destroy(current.getId());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("Co2EmissionsDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getCo2EmissionsCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findCo2EmissionsEntities(1, selectedItemIndex).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }
    
    public String nextHome() {
        getPagination().nextPage();
        recreateModel();
        return "Home";
    }

    public String previousHome() {
        getPagination().previousPage();
        recreateModel();
        return "Home";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findCo2EmissionsEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findCo2EmissionsEntities(), true);
    }

    @FacesConverter(forClass = Co2Emissions.class)
    public static class Co2EmissionsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            Co2EmissionsController controller = (Co2EmissionsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "co2EmissionsController");
            return controller.getJpaController().findCo2Emissions(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Co2Emissions) {
                Co2Emissions o = (Co2Emissions) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Co2Emissions.class.getName());
            }
        }

    }

}

package es.udc.ws.app.model.excursion;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlExcursionDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlExcursionDaoFactory.className";
    private static SqlExcursionDao dao = null;
    private SqlExcursionDaoFactory(){}

    @SuppressWarnings("rawtypes")
    private static SqlExcursionDao getInstance(){
        try{
            String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlExcursionDao) daoClass.getDeclaredConstructor().newInstance();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public synchronized static SqlExcursionDao getDao(){
        if (dao == null){ dao = getInstance();}
        return dao;
    }
}
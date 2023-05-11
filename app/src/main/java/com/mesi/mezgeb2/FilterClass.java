package com.mesi.mezgeb2;

public class FilterClass {

    private int filter_data;
    private static FilterClass filterClassInstance = null;

    public int getFilter_data(){

        return filter_data;
    }

    public void setFilter_data(int filter_data){

        this.filter_data = filter_data;
    }

    private FilterClass(){

    }

    public static FilterClass getFilterInstance(){
        if (filterClassInstance ==null)
        {
            filterClassInstance = new FilterClass();
        return filterClassInstance;
        }

        return filterClassInstance;
    }
}

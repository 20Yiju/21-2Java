package edu.handong.csee.java.hw2.converters;

public interface Convertible {
    /**
     * This is setvalue method
     * @param fromValue this is input Value;original Value
     */
    public void setFromValue(double fromValue);
    /**
     * This is get converted method
     * @return return converted Value
     */
    public double getConvertedValue();
    /**
     * this is converting method
     */
    public void convert();

}
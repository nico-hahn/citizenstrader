package de.qweide.citizenstrader.data;

public class Tuple<T, U> {

    private T firstValue;

    private U secondValue;

    public Tuple(T first, U second) {
        this.firstValue = first;
        this.secondValue = second;
    }

    @Override
    public boolean equals(Object t) {
        if (!(t instanceof Tuple))
            return false;
        return
            this.firstValue.equals(((Tuple)t).firstValue) &&
            this.secondValue.equals(((Tuple)t).secondValue);
    }

    public U getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(U secondValue) {
        this.secondValue = secondValue;
    }

    public T getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(T firstValue) {
        this.firstValue = firstValue;
    }
}

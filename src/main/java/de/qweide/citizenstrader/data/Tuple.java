package de.qweide.citizenstrader.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class Tuple<T, U> implements JsonSerializable {

    private T firstValue;

    private U secondValue;

    public Tuple(T first, U second) {
        this.firstValue = first;
        this.secondValue = second;
    }

    public Tuple() {
        firstValue = null;
        secondValue = null;
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

    @Override
    public JSONObject toJson() {
        return new JSONObject()
            .put("first", firstValue)
            .put("second", secondValue);
    }

    @Override
    public Tuple<T, U> fromJson(String json) throws IOException {
        JSONObject data = new JSONObject(json);
        return new Tuple<T, U>(
            (T)data.get("first"),
            (U)data.get("second")
        );
    }
}

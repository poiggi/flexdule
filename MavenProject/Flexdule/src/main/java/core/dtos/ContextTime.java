package core.dtos;

import core.util.Time;

public class ContextTime {

    protected Time value;
    protected String name;
    protected Time min;
    protected Time max;

    public void valueCopy(Time time) {
        if (time != null) {
            value = Time.copy(time);
        }
    }

    public boolean isValid() throws NullPointerException {
        boolean valid = true;
        try {

            if (min != null)
                valid = value.greaterOrEqualTo(min);

            if (valid && max != null)
                valid = value.lessOrEqualTo(max);

        }catch (NullPointerException e){
            throw e;
        }
        return valid;
    }

    public Time getValue() {
        return value;
    }

    public void setValue(Time value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getMin() {
        return min;
    }

    public void setMin(Time min) {
        this.min = min;
    }

    public Time getMax() {
        return max;
    }

    public void setMax(Time max) {
        this.max = max;
    }
}

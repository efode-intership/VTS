package vn.efode.vts.model;

import java.io.Serializable;

/**
 * Created by Nam on 12/04/2017.
 */

public class WarningTypes implements Serializable {
    private int warningTypeId;
    private String description;
    private int defaultTime;
    private String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWarningTypeId() {
        return warningTypeId;
    }

    public void setWarningTypeId(int warningTypeId) {
        this.warningTypeId = warningTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDefaultTime() {
        return defaultTime;
    }

    public void setDefaultTime(int deafaultTime) {
        this.defaultTime = deafaultTime;
    }

}

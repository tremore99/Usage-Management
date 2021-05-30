package com.example.usagemanagement;

import android.net.Uri;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Usage {
    private String description;
    private String status;
    private String usageDate;
    private String usageType;

    public Usage() {}

    public Usage( String description, String status, String usageDate, String usageType) {
        this.description = description;
        this.status = status;
        this.usageDate = usageDate;
        this.usageType = usageType;
    }


    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getUsageDate() {
        return usageDate;
    }

    public String getUsageType() {
        return usageType;
    }
}

package com.mobilelab.artyomska.planecatalog.model;

/**
 * Created by Artyomska on 11/6/2017.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Artyomska on 10/16/2017.
 */
@Entity(indices={@Index(value="planeName", unique=true)})
public class Plane
{
    @PrimaryKey(autoGenerate = true) @NotNull
    private int ID;
    private String planeName;
    private String planeEngine;
    private String planeProducer;
    private String planeCountry;
    private int planeYear;
    private String wikiLink;

    public Plane()
    {

    }

    @NotNull
    public int getID() {
        return ID;
    }

    public void setID(@NotNull int ID) {
        this.ID = ID;
    }

    public String getPlaneName() {
        return planeName;
    }

    public void setPlaneName(String planeName) {
        this.planeName = planeName;
    }

    public String getPlaneEngine() {
        return planeEngine;
    }

    public void setPlaneEngine(String planeEngine) {
        this.planeEngine = planeEngine;
    }

    public String getPlaneProducer() {
        return planeProducer;
    }

    public void setPlaneProducer(String planeProducer) {
        this.planeProducer = planeProducer;
    }

    public String getWikiLink() {
        return wikiLink;
    }

    public void setWikiLink(String wikiLink) {
        this.wikiLink = wikiLink;
    }

    public String getPlaneCountry() {
        return planeCountry;
    }

    public void setPlaneCountry(String planeCountry) {
        this.planeCountry = planeCountry;
    }

    public int getPlaneYear() {
        return planeYear;
    }

    public void setPlaneYear(int planeYear) {
        this.planeYear = planeYear;
    }

    @Ignore
    public Plane(@NotNull int ID, String planeName, String planeEngine, String planeProducer, String planeCountry, int planeYear) {
        this.ID = ID;
        this.planeName = planeName;
        this.planeEngine = planeEngine;
        this.planeProducer = planeProducer;
        this.planeCountry = planeCountry;
        this.planeYear = planeYear;
    }

    @Ignore
    public Plane(String planeName, String planeEngine, String planeProducer, String planeCountry, int planeYear) {
        this.planeName = planeName;
        this.planeEngine = planeEngine;
        this.planeProducer = planeProducer;
        this.planeCountry = planeCountry;
        this.planeYear = planeYear;
    }

    @Ignore
    public Plane(String planeName, String planeEngine, String planeProducer, String planeCountry, int planeYear,String wikiLink) {
        this.planeName = planeName;
        this.planeEngine = planeEngine;
        this.planeProducer = planeProducer;
        this.planeCountry = planeCountry;
        this.planeYear = planeYear;
        this.wikiLink = wikiLink;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "planeName='" + planeName + '\'' +
                ", planeEngine='" + planeEngine + '\'' +
                ", planeProducer='" + planeProducer + '\'' +
                ", planeCountry='" + planeCountry + '\'' +
                ", planeYear=" + planeYear +
                ", wikiLink='" + wikiLink + '\'' +
                '}';
    }
}


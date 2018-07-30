package com.ewind.hl.persist;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class EventEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String date;
    private String note;
    private String value;
    private String type;
    private int score;
    private String area;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "EventEntity{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", note='" + note + '\'' +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", score='" + score + '\'' +
                ", area=" + area +
                '}';
    }
}

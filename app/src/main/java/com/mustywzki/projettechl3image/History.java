package com.mustywzki.projettechl3image;

import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.List;

public class History {
    private int size;
    private int indCurPicture;
    private List<Bitmap> history;
    private int top;

/* --- Getter Setter --- */
    public int getSize(){
        return size;
    }
    public void setSize(int size){
        this.size = size;
    }

    public int getIndCurPicture() {
        return indCurPicture;
    }
    public void setIndCurPicture(int indCurPicture) {
        this.indCurPicture = indCurPicture;
    }

    public List<Bitmap> getHistory() {
        return history;
    }
    public void setHistory(List<Bitmap> history) {
        this.history = history;
    }

    public int getTop(){
        return top;
    }
    public void setTop(int top){
        this.top = top;
    }

    public History(int size, Bitmap curPicture){
        this.size = size;
        history = new ArrayList<>();
        history.add(curPicture);
        indCurPicture = 0;
        top = indCurPicture;
    }


/* --- Functions --- */
    public boolean addElement(Bitmap picture){
        if (top < size-1){
            indCurPicture++;
            history.add(picture);
        }
        else if (top == size-1){
            history.remove(0);
            history.add(picture);
        }
        else{
            System.out.println("ERROR ! History ind_cur_picture is more than size !");
            return false;
        }
        top = indCurPicture;
        return true;
    }

    public Bitmap getCur_picture(){
        return history.get(indCurPicture);
    }

    public void reset(Bitmap cur_picture){
        history.clear();
        history.add(cur_picture);
        indCurPicture = 0;
        top = indCurPicture;
    }
}

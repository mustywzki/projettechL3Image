package com.mustywzki.projettechl3image;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

class History {
    private int size;
    private int indCurPicture;
    private List<Bitmap> history;
    private int top;

/* --- Getter Setter --- */
    /**
     * Returns indCurPicture.
     * @return indCurPicture
     */
    public int getIndCurPicture() {
        return indCurPicture;
    }

    /**
     * Set indCurPicture.
     * @param indCurPicture the new indice of the current picture
     */
    public void setIndCurPicture(int indCurPicture) {
        this.indCurPicture = indCurPicture;
    }

    /**
     * Returns top.
     * @return top
     */
    public int getTop(){
        return top;
    }

    /**
     * Constructor of the History Class
     * @param size the size of the new History
     * @param curPicture the current picture on the application
     */
    public History(int size, Bitmap curPicture){
        this.size = size;
        history = new ArrayList<>();
        history.add(curPicture);
        indCurPicture = 0;
        top = indCurPicture;
    }


    /**
     * Adds a bitmap image in the History class
     * @param picture bitmap image to add
     */
/* --- Functions --- */
    public void addElement(Bitmap picture){
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
            return;
        }
        top = indCurPicture;
    }

    /**
     * @return Current picture
     */
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

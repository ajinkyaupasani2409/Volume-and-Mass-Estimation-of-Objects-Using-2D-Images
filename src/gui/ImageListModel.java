/*
 * ImageListModel.java
 *
 *  Copyright (C) 2007 COMP5425 Multimedia Storage, Retrieval and Delivery
 *  The School of Information Technology
 *  The University of Sydney
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package gui;

import com.google.code.jcbir.search.ImageResult;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author Yuezhong Zhang  SID:305275631
 */
public class ImageListModel extends AbstractListModel {
    
    private ArrayList<ImageResult> list;
    private int pageSize;
    private int pageOffset;
    
    public ImageListModel() {
        this.pageSize   = 50;
        this.pageOffset = 0;
        list = new ArrayList<ImageResult> ();
    }
    
    public int getSize() {
        if(list.size() == 0)
            return 0;
        if(getPageOffset() == getPageCount() -1)
            return list.size() - (getPageOffset() * getPageSize());
        return pageSize;
    }
    
    public Object getElementAt(int row) {
        int realRow = row + (getPageOffset() * getPageSize());
        return list.get(realRow);
    }
    public int getPageOffset() {
        return pageOffset;
    }
    
    public void setPageOffset(int pageOffset) {
        this.pageOffset = pageOffset;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int s) {
        if (s == pageSize) { return; }
        int oldPageSize = pageSize;
        pageSize = s;
        pageOffset = (oldPageSize * pageOffset) / pageSize;
        fireListDataChanged();
    }
    public int getPageCount(){
        return (int)Math.ceil((double)list.size() / pageSize);
    }
    public int getRealRowCount() {
        return  list.size();
    }
    public void pageUp(){
        if (pageOffset > 0) {
            pageOffset--;
            fireListDataChanged();
        }
    }
    public void pageDown(){
        if (pageOffset < getPageCount() - 1) {
            pageOffset++;
            fireListDataChanged();
        }
    }
    public boolean canPageUp(){
        if(getPageOffset()==0)
            return false;
        return true;
    }
    public boolean canPageDown(){
        return getPageOffset() < getPageCount()-1 ;
    }
    private void fireListDataChanged(){
        fireIntervalRemoved(this, 0, getSize()-1);
        fireIntervalAdded(this, 0, getSize()-1);
    }
    public void clear() {
        list.clear();
        fireIntervalRemoved(this, 0, getSize());
    }
    public void add(ImageResult obj){
        list.add(obj);
        fireListDataChanged();
        
    }
    public void add(List<ImageResult> rs){
        list.addAll(rs);
        fireListDataChanged();
    }
}

package com.example.jessemitchell.popularmovies.app;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jesse.mitchell on 1/17/2017.
 *
 * ExandableListAdapter Resources
 * https://developer.android.com/reference/android/widget/ExpandableListView.html
 * http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter
{

    private static Context mContext;
    private static List<String> mListGroupHeader = new ArrayList<>();
    private static HashMap<String, List<String>> mListChildren = new HashMap<>();

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData)
    {
        this.mContext = context;
        this.mListGroupHeader = listDataHeader;
        this.mListChildren.putAll(listChildData);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String groupHeader = (String) getGroup(groupPosition);
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_detail_expand_group, null);
        }

        TextView groupListHeader = (TextView) convertView.findViewById(R.id.movie_detail_list_header);
        groupListHeader.setTypeface(null, Typeface.BOLD);
        groupListHeader.setText(groupHeader);

        return convertView;
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return this.mListGroupHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return this.mListGroupHeader.size();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        final String childText = (String) getChild(groupPosition,childPosition);

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_detail_expand_group_item,null);
        }

        TextView listChild = (TextView) convertView.findViewById(R.id.movie_detail_list_item);
        listChild.setText(childText);
        return listChild;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return this.mListChildren.get(this.mListGroupHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return this.mListChildren.get(this.mListGroupHeader.get(groupPosition)).size();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

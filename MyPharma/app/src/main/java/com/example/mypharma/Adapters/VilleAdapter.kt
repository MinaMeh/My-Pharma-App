package com.example.mypharma.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.mypharma.Models.Ville
import com.example.mypharma.R

class VilleAdapter(_ctx: Context, _listVilles:List<Ville>): BaseAdapter() {
        var ctx = _ctx
        val listVilles = _listVilles


        override fun getItem(p0: Int) = listVilles.get(p0)

        override fun getItemId(p0: Int) = listVilles.get(p0).hashCode().toLong()

        override fun getCount()= listVilles.size

        override fun getView(position: Int, p0: View?, parent: ViewGroup?): View {

            var view = p0
            var viewHolder: ViewHolder
            if(view == null) {
                view = LayoutInflater.from(ctx).inflate(R.layout.ville_layout,parent,false)
                val name = view?.findViewById<TextView>(R.id.ville_name) as TextView
                viewHolder= ViewHolder(name)
                view.setTag(viewHolder)
            }
            else {
                viewHolder = view.getTag() as ViewHolder

            }

            viewHolder.name.setText(listVilles.get(position).nom)
            return view

        }

        private data class ViewHolder(var name:TextView)
    }

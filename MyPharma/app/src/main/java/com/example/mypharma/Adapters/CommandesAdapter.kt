package com.example.mypharma.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.mypharma.Controllers.CommandesController
import com.example.mypharma.Models.Commande
import com.example.mypharma.R
import com.google.android.material.chip.Chip

class CommandesAdapter(_ctx: Context, _listCommandes:List<Commande>): BaseAdapter() {
    var ctx = _ctx
    val listCommandes = _listCommandes


    override fun getItem(p0: Int) = listCommandes.get(p0)

    override fun getItemId(p0: Int) = listCommandes.get(p0).hashCode().toLong()

    override fun getCount()= listCommandes.size

    override fun getView(position: Int, p0: View?, parent: ViewGroup?): View {

        var view = p0
        var viewHolder: ViewHolder
        if(view == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.commande_layout,parent,false)
            val date = view?.findViewById<TextView>(R.id.date) as TextView
            val pharmacie = view?.findViewById<TextView>(R.id.pharmacie) as TextView
            val etat = view?.findViewById<TextView>(R.id.state) as TextView
            val checked = view?.findViewById<ImageView>(R.id.checked) as ImageView


            viewHolder= ViewHolder(date, pharmacie,etat,checked)
            view.setTag(viewHolder)
        }
        else {
            viewHolder = view.getTag() as ViewHolder

        }

        viewHolder.date.setText(listCommandes.get(position).date)
        val commandes= CommandesController()
        val pharmacie= commandes.getCommandePharmacie(viewHolder,listCommandes.get(position).commande_id)
       // ctx.toast(pharmacie.toString())
        val state=listCommandes.get(position).etat
        if (state=="T"){
            viewHolder.etat.setText("Traitée")
            viewHolder.checked.visibility=View.VISIBLE

        }
        else
        viewHolder.etat.setText(state)
         //viewHolder.pharmacie.setText(pharmacie!!.adresse)

        return view

    }

     data class ViewHolder(var date: TextView, var pharmacie: TextView,var etat:TextView, var checked: ImageView)
}

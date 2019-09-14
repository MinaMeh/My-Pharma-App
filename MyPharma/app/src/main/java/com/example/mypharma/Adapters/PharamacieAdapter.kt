package com.example.mypharma.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.mypharma.Models.Pharmacie
import com.example.mypharma.R

class PharamacieAdapter(_ctx: Context, _listPharmacies:List<Pharmacie>): BaseAdapter() {
    var ctx = _ctx
    val listPharmacies = _listPharmacies


    override fun getItem(p0: Int) = listPharmacies.get(p0)

    override fun getItemId(p0: Int) = listPharmacies.get(p0).hashCode().toLong()

    override fun getCount()= listPharmacies.size

    override fun getView(position: Int, p0: View?, parent: ViewGroup?): View {

        var view = p0
        var viewHolder: ViewHolder
        if(view == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.pharmacie_layout,parent,false)
            val adresse = view?.findViewById<TextView>(R.id.pharmacie_adresse) as TextView
            val horaire = view?.findViewById<TextView>(R.id.horaire) as TextView

            viewHolder= ViewHolder(adresse, horaire)
            view.setTag(viewHolder)
        }
        else {
            viewHolder = view.getTag() as ViewHolder

        }

        viewHolder.addresse.setText(listPharmacies.get(position).adresse)
        viewHolder.horaire.setText("Ouverte de "+listPharmacies.get(position).ouverture+" à "+listPharmacies.get(position).fermeture)

        return view

    }

    private data class ViewHolder(var addresse: TextView, var horaire: TextView)
}

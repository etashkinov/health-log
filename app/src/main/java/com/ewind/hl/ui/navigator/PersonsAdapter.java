package com.ewind.hl.ui.navigator;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ewind.hl.R;
import com.ewind.hl.service.Person;
import com.ewind.hl.ui.UiHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.PersonViewHolder> {

    private List<Person> persons;
    private OnPersonSelectedListener listener;

    public void setPersons(Collection<Person> persons) {
        this.persons = new ArrayList<>(persons);
        Collections.sort(this.persons, (p1, p2) -> p1.getName().compareTo(p2.getName()));
        notifyDataSetChanged();
    }

    public void setListener(OnPersonSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_person_item, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        ImageView photoView = holder.itemView.findViewById(R.id.person_photo);
        Person person = persons.get(position);
        UiHelper.setPersonPhoto(photoView, person.getPhoto(), photoView.getContext());
        photoView.setOnClickListener(v -> listener.onPersonSelected(person));
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    static final class PersonViewHolder extends RecyclerView.ViewHolder {
        public PersonViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnPersonSelectedListener {
        void onPersonSelected(Person person);
    }
}

package com.ewind.hl.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PersonService {

    private static final String TAG = PersonService.class.getSimpleName();

    private static final String PREFERENCES_NAME = "com.ewind.hl.service.PersonService";
    private static final String PERSONS = "PERSONS";
    private static final String CURRENT = "CURRENT";

    private final WeakReference<Context> context;

    public PersonService(Context context) {
        this.context = new WeakReference<>(context);
    }

    public String getCurrentAccountEmail() {
        GoogleSignInAccount account = getCurrentAccount();
        return account == null ? null : account.getEmail();
    }

    private GoogleSignInAccount getCurrentAccount() {
        return GoogleSignIn.getLastSignedInAccount(getContext());
    }

    private String getPreference(String key) {
        return getPreferences().getString(key, null);
    }

    public Set<Person> getInactivePersons() {
        String currentId = getCurrentId();

        Set<String> personIds = new HashSet<>(getContactPersonIds());
        personIds.remove(currentId);

        Set<Person> result = new HashSet<>(getContactPersons(personIds));

        Person accountPerson = getAccountPerson();

        if (accountPerson != null && !accountPerson.getId().equals(currentId)) {
            result.add(accountPerson);
        }

        return result;
    }

    private Person getAccountPerson() {
        GoogleSignInAccount account = getCurrentAccount();
        return account == null ? null : toPerson(account);
    }

    @NonNull
    private Person toPerson(GoogleSignInAccount account) {
        return new Person(account.getEmail(), account.getDisplayName(), account.getPhotoUrl());
    }

    private Set<Person> getContactPersons(Set<String> personIds) {
        CursorLoader loader = new CursorLoader(getContext());
        loader.setUri(ContactsContract.Data.CONTENT_URI);
        loader.setProjection(new String[]{
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data.PHOTO_URI,
        });
        loader.setSelection(ContactsContract.Data.CONTACT_ID + " IN (" + TextUtils.join(",", personIds) + ")");

        try (Cursor cursor = loader.loadInBackground()) {
            Set<Person> result = new HashSet<>();
            while (cursor.moveToNext()) {
                result.add(toPerson(cursor));
            }
            return result;
        }
    }

    private Person getContactPerson(Uri contactUri) {
        CursorLoader cursorLoader = new CursorLoader(getContext());
        cursorLoader.setUri(contactUri);
        cursorLoader.setProjection(new String[]{
                ContactsContract.Data._ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data.PHOTO_URI,
        });
        try (Cursor cursor = cursorLoader.loadInBackground()) {
            cursor.moveToFirst();

            return toPerson(cursor);
        }
    }

    // TODO
    private String getContactEmail(String contactId) {
        CursorLoader loader = new CursorLoader(getContext());
        loader.setUri(ContactsContract.CommonDataKinds.Email.CONTENT_URI);
        loader.setProjection(new String[]{ContactsContract.CommonDataKinds.Email.DATA1});
        loader.setSelection(ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ? " +
                "AND " + ContactsContract.Data.ACCOUNT_TYPE_AND_DATA_SET + " = 'com.google'");
        loader.setSelectionArgs(new String[]{contactId});
        try (Cursor cursor = loader.loadInBackground()) {
            while (cursor.moveToNext()) {
                return cursor.getString(0);
            }
        }

        return null;
    }

    private Person toPerson(Cursor contactCursor) {
        String contactId = contactCursor.getString(0);
        String photoURI = contactCursor.getString(2);
        String name = contactCursor.getString(1);
        return new Person(contactId,
                name,
                photoURI == null ? null : Uri.parse(photoURI));
    }

    @NonNull
    private Set<String> getContactPersonIds() {
        return getPreferences().getStringSet(PERSONS, Collections.emptySet());
    }

    public void setPersons(Set<String> personIds) {
        SharedPreferences preferences = getPreferences();
        SharedPreferences.Editor edit = preferences.edit();
        edit.putStringSet(PERSONS, personIds);
        edit.apply();
    }


    public Person addContactPerson(Uri contactUri) {
        Person person = getContactPerson(contactUri);

        SharedPreferences preferences = getPreferences();
        SharedPreferences.Editor edit = preferences.edit();
        Set<String> owners = new HashSet<>(getContactPersonIds());
        owners.add(person.getId());
        edit.putStringSet(PERSONS, owners);
        edit.apply();

        return person;
    }

    private Person getContactPerson(String contactId) {
        Set<Person> contactPersons = getContactPersons(Collections.singleton(contactId));
        return contactPersons.isEmpty() ? null : contactPersons.iterator().next();
    }

    public void setCurrentId(String currentId) {
        setPreference(CURRENT, currentId);
    }

    public String getCurrentId() {
        return getPreference(CURRENT);
    }

    private void setPreference(String key, String value) {
        SharedPreferences preferences = getPreferences();
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    private Context getContext() {
        return context.get();
    }

    private SharedPreferences getPreferences() {
        return getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public Person getCurrentPerson() {
        String currentId = getCurrentId();
        Person accountPerson = getAccountPerson();
        if (accountPerson != null && accountPerson.getId().equals(currentId)) {
            setCurrentId(accountPerson.getId());
            return accountPerson;
        } else {
            return getContactPerson(currentId);
        }
    }
}

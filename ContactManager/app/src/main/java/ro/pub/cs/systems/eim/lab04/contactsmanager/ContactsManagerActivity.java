package ro.pub.cs.systems.eim.lab04.contactsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import ro.pub.cs.systems.eim.lab04.contactsmanager.general.Constants;


public class ContactsManagerActivity extends AppCompatActivity {

    private EditText name;
    private EditText phoneNumber;
    private EditText email;
    private EditText address;
    private EditText jobTitle;
    private EditText company;
    private EditText website;
    private EditText im;

    private Button showHide;
    private Button save;
    private Button cancel;

    private LinearLayout additionalFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);

        showHide = (Button)findViewById(R.id.show_hide_additional_fields);
        save = (Button)findViewById(R.id.save_button);
        cancel = (Button)findViewById(R.id.cancel_button);

        name = (EditText)findViewById(R.id.name_edit_text);
        phoneNumber = (EditText)findViewById(R.id.phone_number_edit_text);
        email = (EditText)findViewById(R.id.email_edit_text);
        address = (EditText)findViewById(R.id.address_edit_text);
        jobTitle = (EditText)findViewById(R.id.job_title_edit_text);
        company = (EditText)findViewById(R.id.company_edit_text);
        website = (EditText)findViewById(R.id.website_edit_text);
        im = (EditText)findViewById(R.id.im_edit_text);

        showHide.setOnClickListener(showHideListener);
        save.setOnClickListener(saveListener);
        cancel.setOnClickListener(cancelListener);

        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                phoneNumber.setText(phone);
            }
            else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }

    }

    private ShowHideDetailsClickListener showHideListener = new ShowHideDetailsClickListener();
    private class ShowHideDetailsClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            additionalFields = (LinearLayout)findViewById(R.id.additional_fields_container);
            if (additionalFields.getVisibility() == View.VISIBLE) {
                additionalFields.setVisibility(View.INVISIBLE);
            }
            else {
                additionalFields.setVisibility(View.VISIBLE);
            }
        }
    }

    private SaveButtonClickListener saveListener = new SaveButtonClickListener();
    private class SaveButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String nameSave = name.getText().toString();
            String phoneNumberSave = phoneNumber.getText().toString();
            String emailSave = email.getText().toString();
            String addressSave = address.getText().toString();
            String jobTitleSave = jobTitle.getText().toString();
            String companySave = company.getText().toString();
            String websiteSave = website.getText().toString();
            String imSave = im.getText().toString();

            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            if (name != null) {
                intent.putExtra(ContactsContract.Intents.Insert.NAME, nameSave);
            }
            if (phoneNumber != null) {
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumberSave);
            }
            if (email != null) {
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, emailSave);
            }
            if (address != null) {
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, addressSave);
            }
            if (jobTitle != null) {
                intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitleSave);
            }
            if (company != null) {
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, companySave);
            }
            ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
            if (website != null) {
                ContentValues websiteRow = new ContentValues();
                websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, websiteSave);
                contactData.add(websiteRow);
            }
            if (im != null) {
                ContentValues imRow = new ContentValues();
                imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                imRow.put(ContactsContract.CommonDataKinds.Im.DATA, imSave);
                contactData.add(imRow);
            }
            intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
            startActivity(intent);
        }
    }

    private CancelButtonClickListener cancelListener = new CancelButtonClickListener();
    private class CancelButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case Constants.CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }
}
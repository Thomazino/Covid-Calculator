package com.example.helloapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Propabilities extends AppCompatActivity {
   private TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propabilities);
        DatabaseAccess dt=DatabaseAccess.getInstance(getApplicationContext());
        dt.open();
        String[] infos=dt.getInfoForCountry("Chad");
        /**Η info παιρνει ως ορισμα το ονομα μιας χωρας και επιστρεφει εναν πινακα
         * με πληροφοριες σχετικα με αυτην σε πινακα 7 θεσεων. Η πρωτη θεση
         * ειναι το Τotal cases για τον κορονοιο της χωρας,η 2η τα total deaths
         * η 3η τα total recovered η 4η τα active cases η 5η τα Critical cases
         * η 6η το sex ratio της καθε χωρας πχ αμα ειναι 1.03 τοτε
         * πληθος αντρων/πληθος γυναικων=1.03. Και τελος η 7η με το population της καθε χωρας.
         * Ολα επιστρεφονται σε string οποτε θα χρειαστει ενα μικρο cast στη πορεια που γινεται με μια συναρτηση*/
        String deathrate=dt.getDeathRateDisease("Cancer");
        /**η συναρτηση getDeathRateDisease παιρνει ως ορισμα μια ασθενεια και σου επιστρεφει την πιθανοτητα
         * (ή αλλιως το deathrate) να πεθανεις απο αυτην με δεδομενο οτι εχεις τον κορονοιο. Η βαση εχει καταχωρημενες
         * τις πιθανοτητες ΜΟΝΟ για τα ορισματα Cardiovascular disease,Diabetes,Chronic respiratory disease
         * Hypertension, Cancer*/
        String[][] Cases=dt.CasesData(45,"Male");
        /**Η συναρτηση CasesData βαση της ηλικιας και του φυλου με δεδομενα απο 54 states στο κοσμο(δεν επηρεαζει καπως αυτο
         * εχω σβησει τα states) σου επιστρεφει διαφορα καταγεγραμμενα δεδομενα ανα state.Με λιγα λογια εναν πινακα
         * 54x6. Η πρωτη στηλη αντιπροσωπευει τους θανατους απο τον κορονοιο ενος state, η 2η τους συνολικους θανατους
         * που υπηρξαν,η 3η τους θανατους απο πνευμονια,η 4η τους θανατους απο πνευμονια ΚΑΙ κορονοιο,η 5η
         * τους θανατους απο influenza και η 6η τους θανατους ειτε απο κορονοιο ειτε απο πνευμονια ειτε απο influenza.*/
        String[][] GeneralCases=dt.GeneralCasesData();
        /**Κανει τα ιδια με την CasesData απλα τα δεδομενα που επιστρεφει ειναι με βαση ολες τις ηλικιες και ολα τα φυλα.
         * Ο πινακας παλι ειναι 54χ6 με ιδια ακριβως δομη.*/
        String[][] GeneralCasesOnSexes=dt.GeneralCasesOnSexesData("Male Total");
        /**Τα μονα ορισματα που δεχεται ειναι Male Total ή Female Total. Η λειτουργια ειναι ιδια με την Casesdata
         * με την διαφορα οτι τα δεδομενα που θα επιστραφουν ειναι συγκεκριμενα για το φυλο που θελουμε να παρουμε
         * πληροφοριες σε ολες τις ηλικιες που υπαρχει. Παλι επιστρεφεται πινακας 54χ6 με την ιδια δομη που ανελυσα.
         * Γενικα το CasesData εχει σκοπο να επικεντρωθει στα δεδομενα βαση του τι θα βαλει ο χρηστης στο input
         * και οι αλλες ειναι πιο γενικες που ισως να βοηθησουν στο να βγει γενικα μια πιθανοτητα. */
        String[] Countries=dt.getCountries();
        /**Επιστρεφει ολες τις χωρες που εχω στην βαση. Ειναι 177 και ο χρηστης πρεπει να διαλεξει μια απο αυτες
         * αναγκαστικα αρα στο interface θα χρησιμοποιηθει σιγουρα αυτη η συναρτηση ωστε να μπουν αυτες οι χωρες
         * ως επιλογες*/
        dt.close();
    }
}

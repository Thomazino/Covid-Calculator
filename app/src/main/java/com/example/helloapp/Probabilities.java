package com.example.helloapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Probabilities extends AppCompatActivity {
   private TextView txt,txt2,txt3;
   String[] userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propabilities);
        DatabaseAccess dt=DatabaseAccess.getInstance(getApplicationContext());
        dt.open();
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        String[] userData = (String[]) args.getSerializable("STRING");
        if(userData[2].equals("None"))
            userData[2]=null;
        String userSex= userData[0];
        int userAge = Integer.valueOf(userData[1]);
        String userDisease = userData[2];
        String userCountry = userData[3];

        /** Στο String countryInfo αποθηκέυουμε κάποια στοιχεία για την χώρα του χρήστη.
         * Στη πρώτη θέση είναι total cases, 2η total deaths, 3η total recovered, 4η active cases
         * 5η critical cases, 6η sex ratio για την αναλογία αντρών γυναικών στον πληθυσμό της χώρας,
         * 7η ο πληθυσμός της χώρας. */
        String[] countryInfo=dt.getInfoForCountry(userCountry);

        // Ο αριθός όλων των κρουσμάτων που έχουν σημειωθεί στη χώρα
        int countryTotalCases=Integer.valueOf(countryInfo[0]);
        // Ο αριθμός όλων των θανάτων που΄έχουν σημειωθεί στη χώρα
        int countryTotalDeaths=Integer.valueOf(countryInfo[1]);
        // Ο αριθμός όλων των ανθρώπων στην χώρα που έχουν προσβληθεί και έχουν αναρρώσει.
        int countryRecoveredCases=Integer.valueOf(countryInfo[2]);
        // Ο αριθμός των κρουσμάτων που έχουν ακόμα κορωνοιό στην χώρα
        int countryActiveCases=Integer.valueOf(countryInfo[3]);
        // Η τυπική θνητότητα της κάθε χώρας δηλαδή οι καταγεγραμμένοι θάνατοι ανά τα καταγεγραμμένα κρούσματα
        // Η πραγματική θνητότητα κανονικά πρέπει να είναι η αναλογία deaths/closed cases αλλά αυτό είναι αρκετά υψηλότερο ποσοστό
        // όμως σύμφωνα με έρευνες υπάρχουν ασυμπτωματικοί ή άτομα που περνάν τον κορωνοιό χωρίς να κάνουν τεστ οπότε δεν μπορούμε να
        // έχουμε ολοκληρωμένο αληθινό στατιστικό για την πραγματική θνητότητα καθώς οι έρευνες ποικίλουν. Οπότε θα χρησιμοποιήσουμε
        // το ποσοστό θανάτων ανά confirmed cases το οποίο είναι αρκετά κοντά στο πραγματικό.
        float countryDeathRate= (float)countryTotalDeaths/countryTotalCases;
        // Η αναλογία αντρών γυναικών στον πληθυσμό της χώρας.
        float countrySexRatio=Float.valueOf(countryInfo[5]);
        // Ο πληθυσμός της χώρας.
        int countryPopulation=Integer.valueOf(countryInfo[6]);

        // Το σύνολο καταγεγραμμένων κρουσμάτων κορωνοιού σε όλο τον κόσμο.
        int  worldTotalCases= new BigDecimal(dt.getTotalCases()).intValue();
        // Το σύνολο καταγεγραμμένων θανάτων κορωνοιού σε όλο τον κόσμο.
        int worldTotalDeaths=Integer.valueOf(dt.getTotalDeaths());
        // Με την ίδια λογική με την θνητότητα χώρας θα χρησιμοποιήσουμε και το ποσοστό θνητότητας σε όλο τον κοόσμο.
        // Το οποίο βγαίνει γύρω στο 5%. Το πραγματικό σύμφωνα με έρευνες είναι μικρότερο αλλά δεν υπάρχει κάποιο συγκεκριμένο
        // νούμερο που να αποδεικνύεται επιστημονικά καθώς πάλι τα αποτελέσματα ερευνών ποικίλουν.
        // Επίσης δεν υπάρχει κάποια βάση δεδομένων από την οποία μπορούμενα εξάγουμε ένα πιο πραγματικό στατιστικό.
        // Οπότε θα χρησιμοποιήσουμε αυτό, που είναι αρκετά κοντά στο πραγματικό. Ούτως η άλλως η προσσέγγιση της πιθανότητας
        // θανάτου για τον χρήστη υπολογίζεται συγκρίνοντας τα δύο ποσοστά θνητότητας στην χώρα και στον κόσμο οπότε
        // τα δεδομένα της βάσης ικναοποιούν πλήρως την λογική της προσέγγισης της πιθανότητας.
        float worldDeathRate=(float)worldTotalDeaths/worldTotalCases;
        float diseaseDeathRate;
        if (userDisease!=null) {
            // Η συναρτηση getDeathRateDisease παιρνει ως ορισμα μια ασθενεια και  επιστρεφει τo death rate της
            diseaseDeathRate = Float.valueOf(dt.getDeathRateDisease(userDisease))* getUserDiseaseAgeWeight(userAge)/100;

        } else diseaseDeathRate = worldDeathRate;

        String[][] ageCases=dt.CasesData(userAge,userSex);
        // Ο αριθμός θανάτων απο κορωνοιό στην ηλικία του χρήστη στα δεδομένα της βάσης.
        int ageCovidDeaths = Integer.valueOf(ageCases[0][0]);
        // Ο αριθμός θανάτων στην ηλικία του χρήστη στα δεδομένα της βάσης.
        int ageDeaths = Integer.valueOf(ageCases[0][1]);
        // Ο αριθμός θανάτων από κορωνοιό που πέθαναν παθαίνοντας πνευμονία στα δεδομένα της βάσης.
        int agePneumoniaDeaths = Integer.valueOf(ageCases[0][3]);
        // Ο αριθμός θανάτων από απλή γρίπη που σημειώθηκαν το διάστημα έξαρσης του κορωνοιού στις ΗΠΑ.
        int ageInfluenzaDeaths = Integer.valueOf(ageCases[0][4]);


        /** Η βάση δεδομένων που χρησιμοποιήθηκε είνα για δεδομένα των ΗΠΑ μέχρι την ημερομηνία 15/6 με δείγμα
         * 69.000 θανάτους κορωνοιού και 900.000 θανάτους γενικά την περίοδο της έξαρσης του κορωνοιού στις ΗΠΑ.
         * Το δείγμα με 69000 θανάτους μπορεί να θεωρηθεί αρκετά αξιόλογο ώστε να χρησιμοποιηθεί ως δεδομένο για
         * ποσοστά θανάτων ανά ηλικία για όλον τον κόσμο καθώς η ΗΠΑ αποτελεί πολυπολιτισμικό κράτος με πληθυσμό που
         * αποτελείται από όλες τις φυλές του κόσμου κατά πολύ παρόμοιο ποσοστό με τον παγκόσμιο πληθυσμό. Επίσης
         * η θνητότητα στις ΗΠΑ είναι σχεδόν ίδια με τον μέσο όρο του κόσμου ενώ και η ηλικιακή διαστρωμάτωση του
         * πληθυσμού της επίσης κιμένεται στα ίδια επίπεδα με τον παγκόσμιο πληθυσμό. Έτσι μπορούμε να χρησιμοποιήσουμε
         * τα δεδομένα της ΗΠΑ για να εξάγουμε αρκετά ρεαλιστικές προβλέψεις για κάθε χώρα και άνθρωο αν τα προσαρμόσουμε
         * κατάλληλα. Δεν μπορέσαμε να βρούμε βάση δεδομένων που να ικανοποιεί περισσότερο αυτό που χρειάζεται η εφαρμογή
         * αλλά όπως εξήγησα μπορούμε κάλλιστα να υπολογίσουμε πολύ ρεαλιστικά αποτελέσματα. Επίσης να σημειωθεί ότι τα ποσοστά
         * και οι πιθανότητες είναι προσεγγιστηκές, δεν υπάρχει κάποιος τρόπος να υπολογιστεί με απόλυτη ακρίβεια η πραγματική πιθανότητα
         * κάποιος ασθενής να πεθάνει με τα λίγα δεδομένα που υπάρχουν. Για να γίνει κάτι τέτοιο χρειάζεται τεράστιο δείγμα
         * από πολλές χώρες, ηλικίες, φυλές, καθώς και να ληφθούν υπόψιν και εκ γενετής χαρακτηριστικά όπως συγκεκριμένα γονίδια
         * του κάθε ανθρώπου που δύνανται να επηρεάσουν την πιθανότητα θανάτου που ακόμα δεν υπάρχουν κατάλληλα δεδομένα από έρευνες ώστε να
         * αξιολογηθούν κατάλληλα από ένα πρόγραμμα.
         */
        // Στατιστικά της βάσης δεδομένων για το αντρικό φύλο.
        String[][] GeneralCasesMale=dt.GeneralCasesOnSexesData("Male");
        // Στατιστικά της βάσης δεδομένων για το γυναικείο φύλο.
        String[][] GeneralCasesFemale=dt.GeneralCasesOnSexesData("Female");
        // Το σύνολο θανάτων αντρών της βάσης
        int covidMaleDeaths=Integer.valueOf(GeneralCasesMale[0][0]);
        // Το σύνολο θανάτων γυναικών της βάσης
        int covidFemaleDeaths=Integer.valueOf(GeneralCasesFemale[0][0]);
        // Αναλογία θανάτων αντρών/θηλυκών της βάσης προσαρμοσμένο για την κάθε χώρα.
        float sexDeathRatio=(float)covidMaleDeaths/covidFemaleDeaths*countrySexRatio;


        /** Το ποσοστό θανώντων ανάλογα με την ηλικία. Συγκεκριμένα είναι το πηλίκο των θανόντων στο διάστημα ηλικίας και φύλου του χρήστη
         * δια το σύνολο των θανόντων κάθε ηλικίας για το ίδιο φύλο. Το στατιστικό αυτό δεν αντικατοπτρίζει απόλυτα το ποσοστό θανάτων
         * ανά κρουσμάτων για ένα διάστημα ηλικίας καθώς δεν βρέθηκε κατάλληλη βάση δεδομένων για αυτό ακριβώς τον σκοπό. Όμως σύμφωνα με
         * την επιστημοική παραδοχή ότι ο παγκόσμιος πληθυσμός είναι ισοκατανεμημένος ηλικιακά, τα δεδομένα της βάσης απεικονίζουν και τα ποσοστά
         * που θεωρεί η λογική του υπολογισμού των πιθανοτήτων.
         * Όμως επειδή τα δεδομένα ανάλογα με ηλικίες που υπάρχουν στο διαδίκτυο είναι χωρίζονται ανά ηλικιακά διαστήματα, τα αποτελέσματα που εξάγονται
         * πολλές φορές δεν απεικονίζουν την πραγματικότητα. Η βάση δεδομένων χωρίζει τις ηλικίες σε δεκάδες παρέχοντας δεδομένα για κάθε δεκάδα
         * μόνο. Έτσι για κάθε ηλικία σε κάθε δεκάδα εξάγονται τα ίδια ποσοστά, και όταν αλλάζει η διαφορά είναι αισθητή. Για παράδειγμα,
         * για γυνάικα με ηλικία 84 σύμφωνα με την βάση το death rate έίναι 26% ενώ για γυναίκα 85 χρονών είναι 42%. Προφανώς και αυτή η διαφοροποίηση
         * είναι μη ρελιστική οπότε παρακάτω υλοποιείται ένας τρόπος που ανάλογα με την ηλικία σε κάθε ηλικιακή "ομάδα" βγάζει ανάλογο ποσοστό.
         * Για να γίνει αυτό χρείαζεται να γνωρίζει τα αντίστοιχα ποσοστά της επόμενης ηλικιακής ομάδας για να βγεί ένας προσαρμοσμένος μέσος όρος ανάλογα
         * με την ηλικία του χρήστη.
         */
        float ageDeathRate,nextAgeDeathRate;
        // Καλούμε την συνάρτηση που επιστρέφει δεδομένα για την επόμενη ηλικιακή ομάδα.
        String[][] nextAgeCases=dt.CasesData(userAge+10,userSex);
        int nextAgeCovidDeaths = Integer.valueOf(nextAgeCases[0][0]);
        int nextAgeDeaths = Integer.valueOf(ageCases[0][1]);
        int nextAgePneumoniaDeaths = Integer.valueOf(ageCases[0][3]);
        int nextAgeInfluenzaDeaths = Integer.valueOf(ageCases[0][4]);
        // Ανάλογα με το φύλο προσαρμόζει το κλάσμα ώστε να χρησιμοποιηθεί κατάλληλα στην συνάρτηση που υπολογίζει την πιθανότητα θανάτου.
        if (userSex.equals("Female")){
            countrySexRatio=1/countrySexRatio;
            ageDeathRate=(float)ageCovidDeaths/covidFemaleDeaths;
            nextAgeDeathRate=(float)nextAgeCovidDeaths/covidFemaleDeaths;
            sexDeathRatio=1/sexDeathRatio;
        }
        else {
            ageDeathRate=(float)ageCovidDeaths/covidMaleDeaths;
            nextAgeDeathRate=(float)nextAgeCovidDeaths/covidMaleDeaths;
        }

        // Για κάθε μεταβλητή που πρέπει να διαφοροποιείται ανάλογα με την συγκεκριμένη ηλικία του χρήστη καλούμε την συνάρτηση getUserAgeData...
        // Τα βάρη είναι πόσες φορές θα υπολογιστεί η ρίζα, δηλαδή όσο πιο πολύ τείνει να προσεγγίσει το 100% τόσο περισσότερο βάρος
        // άρα τόσες φορές υπολογίζεται η ρίζα, άρα ποτέ δεν φτάνει στο 100.
        ageDeathRate=getUserAgeData(ageDeathRate,nextAgeDeathRate,userAge,(int)Math.ceil(ageDeathRate*diseaseDeathRate*60));
        ageCovidDeaths=(int)getUserAgeData((float)ageCovidDeaths,(float)nextAgeCovidDeaths,userAge,3);
        ageInfluenzaDeaths=(int)getUserAgeData((float)ageInfluenzaDeaths,(float)nextAgeInfluenzaDeaths,userAge,3);
        agePneumoniaDeaths=(int)getUserAgeData((float)agePneumoniaDeaths,(float)nextAgePneumoniaDeaths,userAge,3);


        // String που αποθηκεύει τα ονόματα των 177 χωρών καλώντας την συνάρτηση getCountries. Τα ονόματα είναι αποθηκευμένα αλφαβητικά από την βάση.
        String[] Countries=dt.getCountries();

        // Το ποσοστό επι τοις εκατό πιθανότητας ο χρήστης να πεθάνει αν πρσβληθεί απο κορωνοιό.
   /* */float deathPercentage = getDeathProbability(countryDeathRate, worldDeathRate, ageDeathRate, sexDeathRatio, diseaseDeathRate);
        // Το ποσοστό κάποιος που έχει πεθάνει απο κορωνοιό να κατέληξε λόγω της πνευμονίας.
   /* */float pneumoniaBeforeDeath = getPneumoniaBeforeDeath(ageCovidDeaths,agePneumoniaDeaths);
        // Το ποσοστό επί τοις εκατό της πιθανότητας ο χρήστης που έχει προσβληθεί απο κορωνοιό να πεθάνει παθαίνοντας πνευμονία.
   /* */float pneumoniaCovidPercentage = (deathPercentage*Math.round(pneumoniaBeforeDeath))/100;

        /* Το ποσοστό επί τοις εκατό κάποιος με το φύλο του χρήστη να έχει κορωνοιό.
        * Προφανώς η πιθανότητα δεν αφορά τον εκάστοτε άνθρωπο γιατί επηρεάζουν παράγοντες που δεν απεικονίζονται με δεμένα
        * καθώς κρίνεται κυρίως από τύχη και από την πιθανότητα να συναναστραφεί με ήδη κρούσμα και να προσβληθεί.
        * Για αυτό το ποσοστό αυτό δείχνει μόνο τα active cases / τον πληθυσμό της χώρας με βάση το φύλο.
        * Δηλαδή τα παρακάτω ποσοστά μπορούν να χαρακτηριστούν ως εξής:  */

        // πχ. ποια η πιθανότητα μια ελληνίδα να έχει τώρα κορωνοιό
   /* */float activePercentage = getInfectionProbability(countryActiveCases,countryPopulation,countrySexRatio)*100;
        // πχ. ποια η πιθανότητα ενας νορβηγός να έχει περάσει κορωνοιό.
   /* */float recoveredPercentage = getInfectionProbability(countryRecoveredCases,countryPopulation,countrySexRatio)*100;
        // πχ. ποια η πιθανότητα μια φιλιππινέζα να έχει ψοφίσει απο κορωνοιό.
   /* */float infectedPercentage = getInfectionProbability(countryTotalCases,countryPopulation,countrySexRatio)*100;


        /*  Παρακάτω υπολογίζει την πιθανότητα ο θάνατος κάποιου χωρίς να ξέρουμε κανένα στοιχείο για αυτόν πέρα από την ηλικία του, να οφείλεται σε κορωνοιό.
         *  Δηλαφή το πηλίκο των θανάτων από κορωνοιό στο διάστημα της ηλικίας του δια τον αριθμό όλων των θανάτων στην ηλικία αυτή
         *  από την στιγμή που άρχισε ο κορωνοιός προσαρμοσμένο για κάθε χώρα. */
        float countryCovidRate=(float)countryTotalDeaths/(float)countryPopulation;
        // Επειδή τα δεοδμένα που έχουμε είναι από ΗΠΑ πρέπει να συγκριθεί με την χώρα του χρήστη.
        String[] USAInfo=dt.getInfoForCountry("USA");
        // Ένα ενδεικτικό νούμερο που απεικονίζει το πόσο διαδεμονένος είναι ο κορωνοιός στις ΗΠΑ βάσει θανάτων/πληθυσμό.
        float USACovidRate=(float)Integer.valueOf(USAInfo[1])/(float)Integer.valueOf(USAInfo[6]);
        // Ένας αριθμός που δείχνει πόσο λιγότερο η περισσότερο διαδεδομένος είναι ο κορωνοιός στην χώρα σε σχέση με την χώρα των δεδομένων ΗΠΑ.
        float countryRelativeToData=countryCovidRate/USACovidRate;
        // Η πιθανότητα ο θάνατος κάποιου με τα χαρακτηριστικά του χρήστη να οφείλεται στον κορωνοιό.
   /* */float deathFromCovid = getDeathFromCovidProbability(ageCovidDeaths,ageDeaths,countryRelativeToData);
   /* */float covidVSinfluenza = getCovidVSInfluenza(ageCovidDeaths,ageInfluenzaDeaths,countryRelativeToData);

        txt=(TextView)findViewById(R.id.textView);
        String text1 = "So you are a "+userAge+" year old "+userSex+" from "+userCountry;
        if (userDisease!=null) text1+=(" with "+userDisease);
        else text1+=(" without a chronic disease");
        text1+=(".");
        txt.setText(text1);


        txt2=(TextView)findViewById(R.id.textView2);
        DecimalFormat df = new DecimalFormat();
        int decimal=0;
        if (deathPercentage<10) {
            df.setMaximumFractionDigits(2);
            decimal=2;
        }
        else df.setMaximumFractionDigits(0);
        txt2.setText("The probability you pass away if you get infected with Covid-19 is  "+df.format(deathPercentage)+"-"+df.format(deathPercentage+1/Math.pow(10,decimal))+"%.");
        txt3=(TextView)findViewById(R.id.textView3);
        DecimalFormat df2 = new DecimalFormat();
        df2.setMaximumFractionDigits(3);
        String text2 ="It is "+df.format(pneumoniaCovidPercentage)+"-"+df.format(pneumoniaCovidPercentage+1/Math.pow(10,decimal))+"% possible you pass away from pneumonia caused by the virus.\n\n"
        +"The "+df2.format(infectedPercentage)+" % of "+userSex+"s from "+userCountry+" have been infected with Covid-19 some time until now.\n\n"+
                "The "+df2.format(activePercentage)+" % of "+userSex+"s from "+userCountry+" are now dealing with Covid-19.\n\n"+
                "The "+df2.format(recoveredPercentage)+" % of "+userSex+"s from "+userCountry+" have now recovered from Covid-19.\n\n"
        + "Probability the death of a random "+userAge+" year old "+userSex+" from "+userCountry+" is caused by Covid-19 is around "+df2.format(+deathFromCovid)+"%.\n\n"
        + "Probability a random "+userAge+" year old "+userSex+" develops pneumonia before their death from Covid-19 is around "+df2.format(+pneumoniaBeforeDeath)+"%\n\n"
        + "The cause of death of a random "+userAge+" year old "+userSex+" from "+userCountry+" is ";
        if ((1/covidVSinfluenza)>1.5) text2+=Math.round(1/covidVSinfluenza)+" times more possible to be the common flu than the coronavirus.";
        else if (covidVSinfluenza>1.5) text2+=(int)(covidVSinfluenza+0.5)+ " times more possible to be Covid-19 than the common flu.";
        else text2+=" equally probable to be Covid-19 or the common flu. ";

        txt3.setText(text2);
        dt.close();

    }


    // Η συνάρτηση αυτή υπολογίζει μια πιθανότητα ανάλογα με τα cases που έχει όρισμα, τον πληθυσμό της χώρας και την αναλογία φύλων σε αυτόν.
    float getInfectionProbability(int cases, int population, float sexRatio){
        return cases*sexRatio/population;
    }


    // Η συνάρτηση αυτή υπολογίζει την πιθανότητα κάποιος με τα χαρακτηριστικά του χρήστη να πεθάνει εφόσον έχει προσβληθεί από κορωνοιό.
    float getDeathProbability(float countryDeathRate,float worldDeathRate,float ageDeathRate,float sexDeathRatio, float diseaseDeathRate){

        // Ένα νούμερο που δείχνει πόσο πιο ευάλωτος είναι κάποιος ανάλογα με την χώρα στην οποία ζει.
        // Θεωρώντας ότι όλες οι φυλές έχουν την ίδια "ανθεκτικότητα" στον κορωνοιό, δηλαδή η διαφορά των ποσοστών θνητότητας
        // ανά τις χώρες του κόσμου οφείλεται σε τρίτους παράγοντες όπως:
        // Η δυνατότητα του συστήματος υγείας, καθώς σε πολλές χώρες που υπάρχουν πιο τοπικές εξάρσεις, ο αριθμός των κρουσμάτων που
        // χρειάζονται νοσηλεία υπερβαίνει τις δυνατότητες του συστήματος υγείας με αποτέλεσμα ο θάνατοι να ξεπερνούν τα αναμενόμενα ποσοστά.
        // Επίσης πολλές χώρες έχουν πιο νεανικό πληθυσμό και άλλες πιο γηραιό οπότε είναι αναμενόμενο τα ποσοστά να διαφέρουν αρκετά.
        // Οπότε θα χρειαστέι μια μεταλητή που προσομοιάζει την ρεαλιστική πιθανότητα να αυξομειώνεται το ποσοστό θανάτου ανάλογα με την χώρα
        // με την οποία η διαφορά θνητότητας κάποιας χώρας από τον μέσο όρο έχει μικρό βάρος.
        int weight=20;
        // Η συσχέτιση θνητότητας της χώρας με τον παγκόσμιο μέσο όρο θνησιμότητας.
        float countryDeathRateRelativeToWorld=(worldDeathRate+(countryDeathRate-worldDeathRate)/weight)/worldDeathRate;
        // Το ποσοστό της πιθανότητας να πεθάνει ο χρήστης.
        // Επηρεαζόμενο από την αναλογία θανατων του φύλου του με το αντίθετο, το νουμερο που συσχετίζει την χώρα του με τον παγκόσμιο μέσο όρο
        // το ποσοστό θνητότητας στο διάστημα ηλικίας του, καθώς και το ποσοστό θνητότητας ανάλογα με το αν πάσχει από κάποιο χρόνιο νόσημα.
        float deathProbability= sexDeathRatio*countryDeathRateRelativeToWorld*ageDeathRate*(diseaseDeathRate/worldDeathRate)*100;
        return deathProbability;
    }


    // Συνάρτηση που υπολογίζει την πιθανότητα κάποιος που πέθανε με ίδια ηλικία και χώρα με τον χρήστη να πέθανε από κορωνοιό.
    // Θεωρητικά επηρεάζει μόνο η χώρα καθώς δεν είναι το ίδιο διαδεδομένος ο κορωνοιός σε όλες τις χώρες οπότε απτήν βάση δεδομένων
    // που έχει δεδομένα μόνο από ΗΠΑ συγκρίνουμε το πόσο διαδεδομένος είναι στην χώρα του χρήστη σε σχέση με ΗΠΑ. Επίσης προφανώς
    // συγκρίνει τα ποσοστά θανάτων της ηλικίας αυτής λόγω covid σε σχέση με όλους τους θανάτους στο διάστημα ηλικίας αυτής.
    // Επείδή το δείγμα απο ΗΠΑ είναι πολύ μεγάλο μπορούμε να θεωρήσουμε ότι αντιπροσωπέυει έναν κοινό παγκόσμιο μέσο όρο για όλον τον κόσμο
    // οπότε με την κατάλληλη προσαρμογή για κάθε χώρα εξάγει μια πολυ ρεαλιστική πιθανότητα.
    float getDeathFromCovidProbability(int ageCovidDeaths,int ageDeaths,float countryRelativeToData){
        return (float)ageCovidDeaths*countryRelativeToData/ageDeaths*100;
    }


    // Συνάρτηση που υπολογίζει την πιθανότητα να πάθει πνευμονία και να πεθάνει ο χρήστης.
    float getPneumoniaBeforeDeath(int ageCovidDeaths,int agePneumoniaCovidDeaths){
        return (float)agePneumoniaCovidDeaths/ageCovidDeaths*100;
    }


    // Συνάρτηση που υπολογίζει την αναλογία θανάτων απο κορωνιό σε σχέση με τους θανάτους απο απλή γρίπη ανάλογα με την χώρα.
    // Θεωρώντας ότι τα ποσοστά θανάτων από απλή γρίπη κατά μέσο όρο είναι παρόμοια στον κόσμο, επηρεάζει όμως το πόσο διαδεδομένος
    // είναι ο κορωνοιός στην χώρα οπότε θα χρειαστεί το νούμερο σύγκρισης με τις ΗΠΑ.
    float getCovidVSInfluenza(int ageCovidDeaths,int ageInfluenzaDeaths,float countryRelativeToData){
        return (float)ageCovidDeaths*countryRelativeToData/ageInfluenzaDeaths;
    }

    // Tα ποσοστά για την θνητότητα κάθε ασθένειας είναι πολύ απόλυτα καθώς δεν υπάρχει διαφορά για κάθε ηλικία.
    // Όμως προφανώς επηρεάζει η ηλικία στην πιθανότητα να πεθάνει κάποιος αν έχει κάποια ασθένεια.
    // Δηλαδή όσο πιο μικρός ο χρήστης τόσο πιο πιθανό ένα χρόνιο νόσημα να  επηρεάσει αρνητικά την πιθανότητα να ζήσει.
    // Ετσι σκέφτηκα να δημιουργήσω μια μεταβλτή που ανάλογα με την ηλικία του χρήστη αλλάζει.
    // Όσο πιο μικρός είναι, η πιθανότητα να τον επιρεάζει αρνητικά μια ασθένεια μεγαλώνει εκθετικά. Όπως έρευνες έχουν δείξει
    // σχεδόν όλα τα περιστατικά θανάτων λόγω κορωνοιού σε πολύ νεαρές ηλικίες οφείλονται σε κάποιο χρόνιο νόσημα.
    // Επίσης όσο πιο μεγάλος τόσο λιγότερο επηρεάζει ένα χρόνιο νόσημα την πιθανότητα να ζήσεις καθώς είναι ήδη πολύ βεβαρημένη λόγω
    // ηλικίας και αδύναμου ανασοποιητικού. Η μεταβλητή αυτή είναι αυθαίρετη και δική μου επινόηση, απλά βοηθάει στο να υπολογιστούν πιο
    // ρεαλιστικά αποτέλεσματα βάσει της επιστήμης, καθώς τα δεδομένα που βρήκαμε είναι αρκετά ελειπή στο να δώσουν ρεαλιστική πιθανότητα
    // κρίνοντας από όλα τα χαρακτηριστικά που επιλέγει ο χρήστης.
    // Η μεταβλητή αυτή προκύπτει ως εξής:
    // Διαιρούμε την ηλικία δια 10 ώστε όταν υψωθεί στο τετράωνο να μην διαφοροποιείται πάρα πολυ.
    float getUserDiseaseAgeWeight(int userAge) {
        if (userAge>85) return 1;
        else {
            float userDiseaseAgeWeight = ((float) userAge) / 10;
            // Προσθέτουμε +1 στην περίπτωση που ο χρήστης έχει ηλικία 0-10 και δεν γίνει διαίρεση με 0.
            userDiseaseAgeWeight++;
            // Η θεωρητική συνάρτηση είναι περιττή οπότε την αντανακλούμε βάση τον y άξονα ώστε όσο μικρότερο χ τόσο μεγαλύτερο y.
            // Με το 11 θεωρώ ότι η max ηλικία είναι το 100((11-1)*10 δηλαδή αντίθετα οι πράξεις που έγιναν).
            userDiseaseAgeWeight = (float) (Math.sqrt(9.5) / Math.sqrt(userDiseaseAgeWeight));
            // Υψώνουμε στο τετράγωνο ώστε η συνάρτηση να γίνει εκθετική.
            userDiseaseAgeWeight = (float) Math.pow(userDiseaseAgeWeight, 1.2);
            // Την βάζουμε στη μεταβλητή diseaseDeathRate.
            return userDiseaseAgeWeight;
        }
    }


    // Η συνάρτηση αυτή υπολογίζει έναν μέσο όρο προσαρμοσμένο στην ηλικία του χρήστη.
    // Θεωριτικά χωρίζουμε το διάστημα σε 10 κομμάτια (ηλικίες) με δύο μερίες (υπάρχοντα δεδομένα)
    // Όσο πιο κοντά είναι στην ηλικία που ξεκινάει η μία ηλικιακή ομάδα τόσο προς τα εκεί θα "τείνει" ο προσαρμοσμένος μέσος όρος.
    float getUserAgeData(float a,float b, int userAge, int weight) {
        // Για να διευκολυνθεί η διαφοροποίηση, η κάθε ηλικιακή ομάδα ξεκινάει με Χ5 ηλικία και τελείωνει σε (X+1)4 οπότε μετατρέπουμε
        // το νούμερο βάσει 0-9, και τέλος επιστρέφει το νέο δεδομένο ανάλογα με το πόσο κοντά στο Χ5 και στο (Χ+1)4 είναι η ηλικία του χρήστη.
        if(userAge<=85) {
            userAge += 5;
            userAge = userAge % 10;
            return ((10 - userAge) * a + (userAge) * b) / 10;
        }
        else{
            // Μετά τα 85, επειδή δεν υπάρχουν δεδομένα για μεγαλύτερη ηλικιακή ομάδα, θα φτιάξουμε μια μεταβλητή που παίρνει τιμή ανάλογα με το πόσο
            // πιο μεγάλη είναι η ηλικία του χρήστη από τα 85.
            float x=userAge-85;
            for (int i=0;i<=weight;i++) x=(float)Math.sqrt(x);
            return a*x;
        }
    }
}

package com.example.helloapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Probabilities extends AppCompatActivity {
    private Button Home;
    private TextView txt,txt2,txt3;
    private String[] userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super . onCreate (savedInstanceState);
        setContentView(R.layout.activity_propabilities);
        DatabaseAccess dt = DatabaseAccess.getInstance(getApplicationContext());
        Home =(Button)findViewById(R.id.button12);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void  onClick ( View  v ) {
                GotoActivity(MainActivity.class);
            }
        });
        dt.open();
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        String[] userData = (String[]) args.getSerializable("STRING");
        String[] userDiseases = (String[]) args.getSerializable("STRING DISEASES");
        if (userDiseases[0].equals("None"))
            userDiseases = null;
        String userSex = userData[0];
        int userAge = Integer.valueOf(userData[1]);
        String userCountry = userData[2];

        /* In String countryInfo we store some information about the user's country.
                * In the first place are total cases, 2nd total deaths, 3rd total recovered, 4th active cases
                * 5th critical cases, 6th sex ratio for the proportion of men and women in the population of the country,
         * 7th the population of the country. */
        String[] countryInfo = dt.getInfoForCountry(userCountry);

        // The number of all cases reported in the country
        int countryTotalCases = Integer.valueOf(countryInfo[0]);
        // The number of all deaths that have occurred in the country
        int countryTotalDeaths = Integer.valueOf(countryInfo[1]);
        // The number of all people in the country who have been infected and have recovered.
        int countryRecoveredCases = Integer.valueOf(countryInfo[2]);
        // The number of cases that still have a crown in the country
        int countryActiveCases = Integer.valueOf(countryInfo[3]);
        // The typical mortality of each country, ie the recorded deaths per recorded cases
        // Actual mortality should normally be the deaths / closed cases ratio but this is a much higher rate
        // but according to research there are asymptomatic or people who pass the coronary without doing a test so we can
        // we have complete true statistics on real mortality as research varies. So we will use
        // the death rate per confirmed cases which is quite close to the real one.
        float countryDeathRate = (float) countryTotalDeaths / countryTotalCases;
        // The proportion of men and women in the population of the country.
        float countrySexRatio = Float.valueOf(countryInfo[5]);
        // The population of the country.
        int countryPopulation = Integer.valueOf(countryInfo[6]);

        // The set of recorded coronary cases worldwide.
        int worldTotalCases =  new  BigDecimal (dt . getTotalCases ()) . intValue ();
        // The total recorded coronary deaths worldwide.
        int worldTotalDeaths = Integer.valueOf(dt.getTotalDeaths());
        // By the same logic as country mortality we will use the mortality rate worldwide.
        // Which comes out to around 5%. The real thing according to research is smaller but there is no specific one
        // number to be scientifically proven as research results vary again.
        // There is also no database from which we can extract a more realistic statistic.
        // So we will use this, which is quite close to the real thing. Anyway the approximation of probability
        // death rate for the user is calculated by comparing the two mortality rates in the country and in the world then
        // the database data fully satisfies the logic of the probability approach.
        float worldDeathRate = (float) worldTotalDeaths / worldTotalCases;
        float diseaseDeathRate;
        if (userDiseases != null) {
            // The getDeathRateDisease function takes an illness as its default and returns its death rate
            diseaseDeathRate = dt.getDeathRateDisease(userDiseases, (float)userAge) * getUserDiseaseAgeWeight(userAge) / 100;

        } else diseaseDeathRate = worldDeathRate;

        String[][] ageCases = dt.CasesData(userAge, userSex);
        // The number of deaths from coronary at the age of the user in the database data.
        int ageCovidDeaths = Integer.valueOf(ageCases[0][0]);
        // The number of deaths at the age of the user in the database data.
        int ageDeaths = Integer.valueOf(ageCases[0][1]);
        // The number of coronary artery deaths due to pneumonia in the baseline data.
        int agePneumoniaDeaths = Integer.valueOf(ageCases[0][3]);
        // The number of deaths from the simple flu that occurred during the coronary heart disease in the United States.
        int ageInfluenzaDeaths = Integer.valueOf(ageCases[0][4]);


        /* The database used is for US data up to 15/6 with sample
         * 69,000 coronary deaths and 900,000 deaths in general during the coronary heart disease in the United States.
         * The sample with 69000 deaths can be considered significant enough to be used as a given for
         * Death rates per age worldwide as the US is a multicultural country with a population that
         * consists of all the races of the world in a very similar percentage to the world population. also
                * Mortality in the US is almost the same as the world average and its age stratification
                * Its population is also at the same level as the world population. So we can use
         * US data to export fairly realistic forecasts for each country and person if we adjust them
                * appropriate. We could not find a database that better satisfies what the application needs
         * but as I explained we can very well calculate very realistic results. Also note that the percentages
                * and the probabilities are approximate, there is no way to calculate the actual probability with absolute accuracy
                * a patient to die with the little data available. It takes a huge sample to do that
                * from many countries, ages, races, as well as to take into account birth characteristics such as specific genes
         * of any person who may affect the probability of death that there is not yet adequate research data to
                * properly evaluated by a program.
         */
        // Men's database statistics.
        String[][] GeneralCasesMale = dt.GeneralCasesOnSexesData("Male");
        // Statistics of the female gender database.
        String[][] GeneralCasesFemale = dt.GeneralCasesOnSexesData("Female");
        // The total deaths of men of the base
        int covidMaleDeaths = Integer.valueOf(GeneralCasesMale[0][0]);
        // The total female deaths of the base
        int covidFemaleDeaths = Integer.valueOf(GeneralCasesFemale[0][0]);
        // Death rate of males / females of the base adjusted for each country.
        // If in a country the sex ratio in the population exceeds, because the probability of death is not too affected
        // from the sheet, we will put the root of the sex ratio as it is more representative. For countries where the ratio is close to 1,
        // hardly affects the root so there is no issue.
        float sexDeathRatio = (float) covidMaleDeaths / covidFemaleDeaths * (float)Math.sqrt(countrySexRatio);


        /* The death rate depending on age. Specifically, it is the quotient of deaths in the age and sex of the user
                * for all deaths of any age for the same sex. This statistic does not fully reflect the death rate
                * per case for an age period as no suitable database was found for this purpose. But according to
                * the scientific assumption that the world population is evenly distributed by age, the baseline data also reflect the percentages
         * considered by the logic of probability calculation.
                * But because the data according to ages that exist on the internet are divided by age intervals, the results that are exported
         * many times they do not reflect reality. The database divides the ages into dozens by providing data for each dozen
         * only. So for every age in every dozen the same percentages are exported, and when it changes the difference is noticeable. For example,
         * for women aged 84 according to the base the death rate is 26% while for a woman 85 years old it is 42%. Obviously this differentiation as well
                * is unrealistic so below is implemented a way that depending on the age in each age "group" produces a similar percentage.
         * To do this you need to know the respective percentages of the next age group to get an adjusted average accordingly
         * with the age of the user.
         */
        float ageDeathRate, nextAgeDeathRate;
        // We call the function that returns data for the next age group.
        String[][] nextAgeCases = dt.CasesData(userAge + 10, userSex);
        int nextAgeCovidDeaths = Integer.valueOf(nextAgeCases[0][0]);
        int nextAgeDeaths = Integer.valueOf(ageCases[0][1]);
        int nextAgePneumoniaDeaths = Integer.valueOf(ageCases[0][3]);
        int nextAgeInfluenzaDeaths = Integer.valueOf(ageCases[0][4]);
        // Adjusts the fraction according to gender to be used appropriately in the function that calculates the probability of death.
        if (userSex.equals("Female")) {
            countrySexRatio = 1 / countrySexRatio;
            ageDeathRate = (float) ageCovidDeaths / covidFemaleDeaths;
            nextAgeDeathRate = (float) nextAgeCovidDeaths / covidFemaleDeaths;
            sexDeathRatio =  1  / sexDeathRatio;
        } else {
            ageDeathRate = (float) ageCovidDeaths / covidMaleDeaths;
            nextAgeDeathRate = (float) nextAgeCovidDeaths / covidMaleDeaths;
        }

        // For each variable that must be differentiated according to the specific age of the user we call the function getUserAgeData ...
        // The weights are how many times the root will be calculated, ie the more it tends to approach 100% the more weight
        // so the root is calculated so many times, so it never reaches 100.
        ageDeathRate = getUserAgeData(ageDeathRate, nextAgeDeathRate, userAge, (int) Math.ceil(ageDeathRate * Math.pow(diseaseDeathRate/worldDeathRate,1.5)));
        ageCovidDeaths = (int) getUserAgeData((float) ageCovidDeaths, (float) nextAgeCovidDeaths, userAge, 3);
        ageInfluenzaDeaths = (int) getUserAgeData((float) ageInfluenzaDeaths, (float) nextAgeInfluenzaDeaths, userAge, 3);
        agePneumoniaDeaths = (int) getUserAgeData((float) agePneumoniaDeaths, (float) nextAgePneumoniaDeaths, userAge, 3);


        // String that stores the names of 177 countries by calling the getCountries function. The names are stored alphabetically from the database.
        String[] Countries = dt.getCountries();

        // Percent chance of the user dying if infected by a coronary artery.
        /* */
        float deathPercentage = getDeathProbability(countryDeathRate, worldDeathRate, ageDeathRate, sexDeathRatio, diseaseDeathRate);
        // The percentage of someone who has died of coronary artery disease due to pneumonia.
        /* */
        float pneumoniaBeforeDeath = getPneumoniaBeforeDeath(ageCovidDeaths, agePneumoniaDeaths);
        // Percentage of chance a user who has been infected with coronary artery disease will die of pneumonia.
        /* */
        float pneumoniaCovidPercentage = (deathPercentage * Math.round(pneumoniaBeforeDeath)) / 100;

        /* Percentage of someone with a user gender having a crown.
         * Obviously the probability does not concern the individual person because they are influenced by factors that are not shown with ties
                * as it is judged mainly by luck and by the possibility of having intercourse with an already case and being infected.
         * For this percentage it shows only the active cases / population of the country based on gender.
         * That is, the following percentages can be characterized as follows:   */

        // eg what is the probability that a Greek woman now has a crown
        /* */
        float activePercentage = getInfectionProbability(countryActiveCases, countryPopulation, countrySexRatio) * 100;
        // eg what is the probability that a Norwegian has passed a crown.
        /* */
        float recoveredPercentage = getInfectionProbability(countryRecoveredCases, countryPopulation, countrySexRatio) * 100;
        // eg what is the probability that a Filipina has died from a coronary artery.
        /* */
        float infectedPercentage = getInfectionProbability(countryTotalCases, countryPopulation, countrySexRatio) * 100;


        /*   Below calculates the probability that someone died without knowing any information about him beyond his age, due to a coronation.
                That is, the quotient of deaths from coronary artery by age for the number of all deaths at that age
         * from the moment the corona began to be adapted for each country. */
        float countryCovidRate = (float) countryTotalDeaths / (float) countryPopulation;
        // Because the data we have is from the US it has to be compared to the user country.
        String[] USAInfo = dt.getInfoForCountry("USA");
        // An indicative number depicting how widespread the coronation is in the US based on deaths / population.
        float USACovidRate = (float) Integer.valueOf(USAInfo[1]) / (float) Integer.valueOf(USAInfo[6]);
        // A number that indicates how less or more widespread the coronation is in the country than in the US data country.
        float countryRelativeToData = countryCovidRate / USACovidRate;
        // The possibility of someone dying with user characteristics due to the coronary artery.
        /* */
        float deathFromCovid = getDeathFromCovidProbability(ageCovidDeaths, ageDeaths, countryRelativeToData);
        /* */
        float covidVSinfluenza = getCovidVSInfluenza(ageCovidDeaths, ageInfluenzaDeaths, countryRelativeToData);


        txt = (TextView) findViewById(R.id.textView);



        String text1 = "So you are a " + userAge + " year old " + userSex + " from " + userCountry;
        if (userDiseases != null) {
            if( ( userDiseases[0].equals("Chronic respiratory disease") || userDiseases[0].equals("Cardiovascular disease") || userDiseases[0].equals("Hypertension") )&& userDiseases.length==1){
                txt.setTextSize(17);
            }
            else if( (userDiseases.length>1 && userDiseases.length<=3) || !userDiseases[0].equals("Cardiovascular disease"))
                txt.setTextSize(15);
            else
                txt.setTextSize(13);
            text1 += (" with " + userDiseases[0]);
            for(int i=1;i<userDiseases.length;i++) {
                if (i==userDiseases.length-1) text1+=(" and "+userDiseases[i]);
                else text1 +=(", "+userDiseases[i]);
            }
        }
        else text1 += (" without a chronic disease");
            text1 += (".");
            txt.setText(text1);


            txt2 = (TextView) findViewById(R.id.textView2);
            DecimalFormat df = new DecimalFormat();
            int decimal = 0;
            if (deathPercentage < 10) {
                df.setMaximumFractionDigits(2);
                decimal = 2;
            } else df.setMaximumFractionDigits(0);
            txt2.setText("The probability you pass away if you get infected with Covid-19 is  " + df.format(deathPercentage) + "-" + df.format(deathPercentage + 1 / Math.pow(10, decimal)) + "%.");
            txt3 = (TextView) findViewById(R.id.textView3);
            DecimalFormat df2 = new DecimalFormat();
            df2.setMaximumFractionDigits(3);
            String text2 = "It is " + df.format(pneumoniaCovidPercentage) + "-" + df.format(pneumoniaCovidPercentage + 1 / Math.pow(10, decimal)) + "% possible you pass away from pneumonia caused by the virus.\n\n"
                    + "The " + df2.format(infectedPercentage) + " % of all " + userSex + "s from " + userCountry + " have been infected with Covid-19 some time until now.\n\n" +
                    "The " + df2.format(activePercentage) + " % of all " + userSex + "s from " + userCountry + " are now dealing with Covid-19.\n\n" +
                    "The " + df2.format(recoveredPercentage) + " % of all " + userSex + "s from " + userCountry + " have now recovered from Covid-19.\n\n"
                    + "Probability the death of a random " + userAge + " year old " + userSex + " from " + userCountry + " is caused by Covid-19 is around " + df2.format(+deathFromCovid) + "%.\n\n"
                    + "Probability a random " + userAge + " year old " + userSex + " develops pneumonia before their death from Covid-19 is around " + df2.format(+pneumoniaBeforeDeath) + "%\n\n"
                    + "The cause of death of a random " + userAge + " year old " + userSex + " from " + userCountry + " is ";
            if (covidVSinfluenza > 100) covidVSinfluenza = 100;
            if (covidVSinfluenza < 1 / 100) covidVSinfluenza = 1 / 100;
            if ((1 / covidVSinfluenza) > 1.5)
                text2 += Math.round(1 / covidVSinfluenza) + " times more possible to be the common flu than the coronavirus.";
            else if (covidVSinfluenza > 1.5)
                text2 += (int) (covidVSinfluenza + 0.5) + " times more possible to be Covid-19 than the common flu.";
            else text2 += " equally probable to be Covid-19 or the common flu. ";
            txt3.setText(text2);
            dt.close();


    }

    // This function calculates a probability depending on the cases that have an argument, the population of the country and the sex ratio in it.
    float getInfectionProbability(int cases, int population, float sexRatio){
        return cases*sexRatio/population;
    }


    // This function calculates the probability that someone with the user characteristics will die if they have been infected by a coronary artery.
    float  getDeathProbability ( float  countryDeathRate , float  worldDeathRate , float  ageDeathRate , float  sexDeathRatio , float  diseaseDeathRate ) {

        // A number that shows how vulnerable one is depending on the country in which one lives.
        // Considering that all races have the same "resistance" to the corona, ie the difference in mortality rates
        // around the world is due to third factors such as:
        // The potential of the health system, as in many countries where there are more local outbreaks, the number of cases reported
        // need hospitalization exceeds the capabilities of the health system resulting in deaths exceeding expected rates.
        // Also many countries have a younger population and others older so the percentages are expected to vary considerably.
        // So you will need a monitor that simulates the realistic probability that the death rate will fluctuate depending on the country
        // with which the difference in mortality of a country from the average has a small weight.
        float weight = (float)0.05;
        // The correlation of the country's mortality with the global average mortality.
        float countryDeathRateRelativeToWorld=(worldDeathRate+(countryDeathRate-worldDeathRate)*weight)/worldDeathRate;
        // The percentage of chance the user will die.
        // Affected by the ratio of sex deaths to the opposite, the number that correlates his country with the world average
        // the mortality rate in the age group, as well as the mortality rate depending on whether he suffers from a chronic disease.
        float deathProbability= sexDeathRatio*countryDeathRateRelativeToWorld*ageDeathRate*(diseaseDeathRate/worldDeathRate)*100*(float)0.45;
        // I tried too many combinations and especially combinations that tend to large numbers and none approached 100. If any combination
        // exceeds 100 for some reason then we put it to show 98-99%.
        if(deathProbability>=99) deathProbability=98;
        return deathProbability;
    }
    // Function that calculates the probability that someone who died at the same age and country as the user died from a coronary artery.
    // Theoretically it affects only the country as the corona is not equally widespread in all countries so the tangible database
    // which has data only from the USA we compare how widespread it is in the user's country in relation to the USA. Also obviously
    // compares the death rates of this age due to covid in relation to all deaths in this age group.
    // Because the sample from the US is very large we can consider it to represent a common global average for the whole world
    // so with the right adjustment for each country it brings out a very realistic probability.
    float  getDeathFromCovidProbability ( int  ageCovidDeaths , int  ageDeaths , float  countryRelativeToData ) {
        return (float)ageCovidDeaths*countryRelativeToData/ageDeaths*100;
    }


    // Function that calculates the probability of pneumonia and the user dies.
    float  getPneumoniaBeforeDeath ( int  ageCovidDeaths , int  agePneumoniaCovidDeaths ) {
        return (float)agePneumoniaCovidDeaths/ageCovidDeaths*100;
    }


    // Function that calculates the proportion of deaths from coronary in relation to deaths from simple influenza depending on the country.
    // Considering that the average death rates from the common cold are similar in the world, but it affects how widespread
    // is the crown in the country so you will need the comparison number with the US.
    float  getCovidVSInfluenza ( int  ageCovidDeaths , int  ageInfluenzaDeaths , float  countryRelativeToData ) {
        return (float)ageCovidDeaths*countryRelativeToData/ageInfluenzaDeaths;
    }

    // The mortality rates for each disease are very absolute as there is no difference for each age.
    // But age obviously affects the chance of someone dying if they have an illness.
    // That is, the smaller the user, the more likely a chronic disease is to adversely affect the likelihood of living.
    // So I thought of creating a variable that changes depending on the age of the user.
    // The younger he is, the more likely he is to be adversely affected by a disease. As research has shown
    // almost all deaths due to coronary artery disease at a very young age are due to a chronic disease.
    // Also the older the less a chronic disease affects the chance of living as it is already very aggravated due to
    // aged and weak respiratory. This variable is arbitrary and my invention, it just helps to calculate more
    // realistic results based on science, as the data we found are quite incomplete to give a realistic probability
    // judging by all the features selected by the user.
    // This variable appears as follows:
    // Divide the age by 10 so that when it rises to the square it does not differ too much.
    float getUserDiseaseAgeWeight(int userAge) {
        if (userAge>85) return 1;
        else {
            float userDiseaseAgeWeight = ((float) userAge) / 10;
            // Add +1 in case the user is aged 0-10 and is not divided by 0.
            userDiseaseAgeWeight ++ ;
            // The theoretical function is redundant so we reflect it based on the y axis so that the smaller x the larger y.
            // With 11 I consider that the max age is 100 ((11-1) * 10 that is the opposite of the operations performed).
            userDiseaseAgeWeight = (float) (Math.sqrt(9.5) / Math.sqrt(userDiseaseAgeWeight));
            // Raise to the square so that the function becomes exponential.
            userDiseaseAgeWeight = (float) Math.pow(userDiseaseAgeWeight, 1.5);
            // Put it in the variable diseaseDeathRate.
            return userDiseaseAgeWeight;
        }
    }


    // This function calculates an average adapted to the age of the user.
    // Theoretically we divide the space into 10 parts (ages) with two parts (existing data)
    // The closer it is to the age at which one age group starts, the more the adjusted average will "tend" towards it.
    float getUserAgeData(float a,float b, int userAge, int weight) {
        // To facilitate differentiation, each age group starts at X5 age and ends at (X + 1) 4 so we convert
        // the number based on 0-9, and finally returns the new data depending on how close to X5 and (X + 1) 4 is the age of the user.
        if(userAge<=85) {
            userAge += 5;
            userAge = userAge % 10;
            return ((10 - userAge) * a + (userAge) * b) / 10;
        }
        else{
            // After 85, because there is no data for an older age group, we will make a variable that gets a value depending on how much
            // the user is older than 85.
            float x=userAge-85;
            for (int i=0;i<=weight;i++) x=(float)Math.sqrt(x);
            return a*x;
        }
    }




    public  void  GotoActivity ( Class  Activity ) {
        Intent intent=new Intent(this,Activity);
        startActivity(intent);
    }




}




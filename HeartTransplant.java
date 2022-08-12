/*************************************************************************
 *  Compilation:  javac HeartTransplant.java
 *  Execution:    java HeartTransplant < data.txt
 *
 *  @author:
 *
 *************************************************************************/

public class HeartTransplant {

    /* ------ Instance variables  -------- */

    // Person array, each Person is read from the data file
    private Person[] listOfPatients;

    // SurvivabilityByAge array, each rate is read from data file
    private SurvivabilityByAge[] survivabilityByAge;

    // SurvivabilityByCause array, each rate is read from data file
    private SurvivabilityByCause[] survivabilityByCause;

    /* ------ Constructor  -------- */
    
    /*
     * Initializes all instance variables to null.
     */
    public HeartTransplant() {
        
        listOfPatients = null;
        survivabilityByAge = null;
        survivabilityByCause = null;
    }


    public int addPerson(Person p, int arrayIndex) {
        
        if ( arrayIndex > listOfPatients.length ) {
            return -1;
        } else {
            listOfPatients[arrayIndex] = p;
            return 0;
        }
    }

    
    public int readPersonsFromFile( int numberOfLines ) {

        listOfPatients = new Person[numberOfLines];
        for ( int x = 0; x < numberOfLines; x++ ) {
            listOfPatients[x] = new Person( StdIn.readInt(), StdIn.readInt(), StdIn.readInt(), StdIn.readInt(), StdIn.readInt(), StdIn.readInt(), StdIn.readInt() );
        }
        return listOfPatients.length;
    }

    
    public int readSurvivabilityRateByAgeFromFile ( int numberOfLines ) {
    
        survivabilityByAge = new SurvivabilityByAge[numberOfLines];    
        for (int x = 0; x < numberOfLines; x++ ) {
            survivabilityByAge[x] = new SurvivabilityByAge(StdIn.readInt(), StdIn.readInt(), StdIn.readDouble());
        }
        return survivabilityByAge.length;
    }

    
    public int readSurvivabilityRateByCauseFromFile (int numberOfLines) {
        survivabilityByCause = new SurvivabilityByCause[numberOfLines];
        for (int x = 0; x < numberOfLines; x++) {
            survivabilityByCause[x] = new SurvivabilityByCause(StdIn.readInt(), StdIn.readInt(), StdIn.readDouble());
        }
        return survivabilityByCause.length;
    }
    
    
    public Person[] getListOfPatients() {
        return listOfPatients;
    } 

    
    public SurvivabilityByAge[] getSurvivabilityByAge() {
        return survivabilityByAge;
    }

    
    public SurvivabilityByCause[] getSurvivabilityByCause() {
        return survivabilityByCause;
    }

     
    public Person[] getPatientsWithAgeAbove(int age) {

        int[] AgeAboveInd = new int[listOfPatients.length];
        int count = 0;
       
        for (int x = 0; x < listOfPatients.length; x++) {
            if (listOfPatients[x].getAge() > age) {
                AgeAboveInd[count] = x;
                count++;
            }
        }

        if ( count == 0 ) {
            return null;
        }

        Person[] AgeAbovePat = new Person[count];
        for ( int x = 0; x < count; x++ ) {
            AgeAbovePat[x] = listOfPatients[AgeAboveInd[x]];
        }
        return AgeAbovePat;
    }
    
    
    public Person[] getPatientsByStateOfHealth(int state) {

        int[] SameHealthInd = new int[listOfPatients.length];
        int count = 0;
        
        for (int x = 0; x < listOfPatients.length; x++) {
            if (listOfPatients[x].getStateOfHealth() == state) {
                SameHealthInd[count] = x;
                count++;
            }
        }

        if (count == 0) {
            return null;
        }
        Person[] SameHealthPat = new Person[count];
       
        for (int x = 0; x < count; x++) {
            SameHealthPat[x] = listOfPatients[SameHealthInd[x]];
        }

        return SameHealthPat;
    }

    
    public Person[] getPatientsByHeartConditionCause(int cause) {

        int[] SameCauseInd = new int[listOfPatients.length];
        int count = 0;
        //determines if there are any patients with the same cause and records their index into indexesWithSameCause
       
        for (int x = 0; x < listOfPatients.length; x++) {
            if (listOfPatients[x].getCause() == cause) {
                SameCauseInd[count] = x;
                count++;
            }
        }

        if (count == 0) {
            return null;
        }
        Person[] SameCausePat = new Person[count];
        for (int x = 0; x < count; x++) {
            SameCausePat[x] = listOfPatients[SameCauseInd[x]];
        }
        return SameCausePat;
    }

    
    public Person[] match(int numberOfHearts) {

        if (numberOfHearts >= listOfPatients.length) {
            return listOfPatients;
        }
       
        int[][] TempArray = new int[listOfPatients.length][3];
        
        for (int x = 0; x < TempArray.length; x++) {
            TempArray[x][0] = x;
            TempArray[x][1] = listOfPatients[x].getAge();
            TempArray[x][2] = listOfPatients[x].getCause();
        }

        int[][] patients = new int[listOfPatients.length][3];
        
        for (int x = 0, ind = 0, lowAge, cause = 0; x < patients.length; x++) {
            lowAge = 200;

            for (int i = 0; i < patients.length; i++) {
                if (TempArray[i][1] < lowAge) {
                    ind = TempArray[i][0];
                    lowAge = TempArray[i][1];
                    cause = TempArray[i][2];
                }
            }

            patients[x][0] = ind;
            patients[x][1] = lowAge;
            patients[x][2] = cause;

            TempArray[ind][1] = 200;
        }
        
        double[][] patientsSurvivability = new double[patients.length][2];
        
        for (int x = 0; x < patients.length; x++) {
           
            double LongTermAge = 0.0;
           
            for (int i = 0; i < survivabilityByAge.length; i++) {
                if (survivabilityByAge[i].getYears() == 5 && patients[x][1] < survivabilityByAge[i].getAge()) {
                    LongTermAge = survivabilityByAge[i].getRate();
                    break;
                }
            }

            double LongTermCause = 0.0;
            for (int j = 0; j < survivabilityByCause.length; j++) {
                if (survivabilityByCause[j].getYears() == 5 && patients[x][2] == survivabilityByCause[j].getCause()) {
                    LongTermCause = survivabilityByCause[j].getRate();
                    break;
                }
            }

            patientsSurvivability[x][0] = patients[x][0];
            patientsSurvivability[x][1] = LongTermAge > LongTermCause ? LongTermCause : LongTermAge;
        }

        Person[] recipients = new Person[numberOfHearts];
        for (int x = 0; x < numberOfHearts; x++) {
            int indA = 0, indB = 0;
            double highRate = 0.0;

            for (int i = 0; i < patientsSurvivability.length; i++) {
                if (patientsSurvivability[i][1] > highRate) {
                    indA = i;
                    indB = (int) patientsSurvivability[i][0];
                    highRate = patientsSurvivability[i][1];
                }
            }

            recipients[x] = listOfPatients[indB];

            patientsSurvivability[indA][1] = 0.0;
        }

        return recipients;
    }

    /*
     * Client to test the methods you write
     */
    public static void main (String[] args) {

        HeartTransplant ht = new HeartTransplant();

        int numberOfLines = StdIn.readInt();
        int numberOfReadings = ht.readPersonsFromFile(numberOfLines);
        StdOut.println(numberOfReadings + " patients read from file.");
 
        numberOfLines = StdIn.readInt();
        numberOfReadings = ht.readSurvivabilityRateByAgeFromFile(numberOfLines);
        StdOut.println(numberOfReadings + " survivability rates by age lines read from file.");
    
        numberOfLines = StdIn.readInt();
        numberOfReadings = ht.readSurvivabilityRateByCauseFromFile(numberOfLines);
        StdOut.println(numberOfReadings + " survivability rates by cause lines read from file.");

        for (Person p : ht.getListOfPatients()) {
            StdOut.println(p);
        }

        for (SurvivabilityByAge rate : ht.getSurvivabilityByAge()) {
            StdOut.println(rate);
        }

        for (SurvivabilityByCause rate : ht.getSurvivabilityByCause()) {
            StdOut.println(rate);
        }
    }
}
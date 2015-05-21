//source: http://stackoverflow.com/questions/955110/similarity-string-comparison-in-java
package evaluation;

public class StringSimilarity {

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     */
    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */ }
        /* // If you have StringUtils, you can use it to calculate the edit distance:
         return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter)) /
         (double) longerLength; */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://rosettacode.org/wiki/Levenshtein_distance#Java
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

    public static void printSimilarity(String s, String t) {
        System.out.println(String.format(
                "%.3f is the similarity between \"%s\" and \"%s\"", similarity(s, t), s, t));
    }

    public static void printSimilarityShort(String s, String t) {
        System.out.println(String.format(
                "%.3f", similarity(s, t)));
    }

//    public static void main(String[] args) throws Exception {
////        printSimilarity("", "");
////        printSimilarity("1234567890", "1");
////        printSimilarity("1234567890", "123");
////        printSimilarity("1234567890", "1234567");
////        printSimilarity("1234567890", "1234567890");
////        printSimilarity("1234567890", "1234567980");
////        printSimilarity("47/2010", "472010");
////        printSimilarity("47/2010", "472011");
////        printSimilarity("47/2010", "AB.CDEF");
////        printSimilarity("47/2010", "4B.CDEFG");
////        printSimilarity("47/2010", "AB.CDEFG");
////        printSimilarity("The quick fox jumped", "The fox jumped");
////        printSimilarity("The quick fox jumped", "The fox");
////        printSimilarity("kitten", "sitting");
////        printSimilarity("dog", "dog");
//
//        ViewXML xml = new ViewXML();
//        String result3 = xml.getText("test/cas/results/00ef1c115337f9822597da4e1b2d0f6b.xml");
//        String manual3 = xml.getText("test/cas/manual/00ef1c115337f9822597da4e1b2d0f6b.xml");
//
//        System.out.println(result3);
//        System.out.println();
//        System.out.println(manual3);
//
//        printSimilarity(result3, manual3);
//        //iny nastroj, co robi diff
//        //https://code.google.com/p/google-diff-match-patch/
//
//        String string1 = "Prokuratúra po troch týždňoch polícii: Hľadajte svedkov incidentu v električke! Martin (28) z bratislavskej Petržalky je podozrivý, že v pondelok 2. marca v električke číslo 4 na Americkom námestí v mestskej časti Staré Mesto napadol ženu (39). Mal ju biť do tváre. Po vystúpení z električky na zastávke konflikt pokračoval. Martin ženu ťahal za vlasy a strhol jej kabelku. Policajti, ktorí podozrivého zatkli krátko po čine, Martina obvinili z výtržnosti. V prípade dokázania viny na súde mu hrozí trest odňatia slobody až na tri roky - informoval bratislavský krajský policajný hovorca Michal Szeiff.  Polícia sa vo štvrtok 26. marca prostredníctvom médií obrátila na verejnosť so žiadosťou o nájdenie prípadných svedkov incidentu. Pokyn na hľadanie prípadných svedkov po troch týždňoch od incidentu dal dozorujúci prokurátor.  Martin (28) z bratislavskej Petržalky je podozrivý, že v pondelok 2. marca v električke číslo 4 na Americkom námestí v mestskej časti Staré Mesto napadol ženu (39). Mal ju biť do tváre. Po vystúpení z električky na zastávke konflikt pokračoval. Martin ženu ťahal za vlasy a strhol jej kabelku. Policajti, ktorí podozrivého zatkli krátko po čine, Martina obvinili z výtržnosti. V prípade dokázania viny na súde mu hrozí trest odňatia slobody až na tri roky - informoval bratislavský krajský policajný hovorca Michal Szeiff. ";
//        String string2 = "Martin (28) z bratislavskej Petržalky je podozrivý, že v pondelok 2. marca v električke číslo 4 na Americkom námestí v mestskej časti Staré Mesto napadol ženu (39). Mal ju biť do tváre. Po vystúpení z električky na zastávke konflikt pokračoval. Martin ženu ťahal za vlasy a strhol jej kabelku. Policajti, ktorí podozrivého zatkli krátko po čine, Martina obvinili z výtržnosti. V prípade dokázania viny na súde mu hrozí trest odňatia slobody až na tri roky - informoval bratislavský krajský policajný hovorca Michal Szeiff.  Polícia sa vo štvrtok 26. marca prostredníctvom médií obrátila na verejnosť so žiadosťou o nájdenie prípadných svedkov incidentu. Pokyn na hľadanie prípadných svedkov po troch týždňoch od incidentu dal dozorujúci prokurátor. ";
//        printSimilarity(string1, string2);
//    }
}

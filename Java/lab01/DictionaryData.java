
/**
 *
 * @author lewi0146
 */
public class DictionaryData {

    String ranking;
    String word;
    String frequency;

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    /**
     * Creates a new DictionaryData object based upon the the String line that
     * contains the data about the new data item.
     *
     * @param line the data about the new data item
     */
    public DictionaryData(String line)
    {
        String[] input = line.split(" ");
        ranking = input[0];
        word = input[1];
        frequency = input[2];
    }

    /**
     * A string representation of the DataDictionary object. For example
     *
     *     "orange: frequency = 518"
     *
     * @return a string representation of the DataDictionary object
     */
    @Override
    public String toString() {
        return word + ": " + "frequency" + " = " + frequency;
    }
}

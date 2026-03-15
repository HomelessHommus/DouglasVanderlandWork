
/**
 *
 * @author lewi0146
 */
public class DictionaryData {

    String ranking;
    String word;
    int frequency;

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getWord() {
        return word.toLowerCase();
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
     * Creates a new DictionaryData object based upon the String line that
     * contains the data about the new data item.
     *
     * @param line the data about the new data item
     */
    public DictionaryData(String line)
    {
        String[] input = line.split(" ");
        ranking = input[0];
        word = input[1];
        frequency = Integer.parseInt(input[2]);
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

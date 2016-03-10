import java.text.DecimalFormat;

public class TestFile {
    private String emailName;
    private String actualClass;
    private double spamProbability;
    
    public TestFile(String emailName, String actualClass, double spamProbability) {
        this.emailName = emailName;
        this.actualClass = actualClass;
        this.spamProbability = spamProbability;
    }
    
    public String getEmailName() { return this.emailName; }
    public String getActualClass() { return this.actualClass; }
    public double getSpamProbability() { return this.spamProbability; }
    
    public void setEmailName(String emailName) { this.emailName = emailName; }
    public void setActualClass(String actualClass) { this.actualClass = actualClass; }
    public void setSpamProbability(double spamProbability) { this.spamProbability = spamProbability; }
}
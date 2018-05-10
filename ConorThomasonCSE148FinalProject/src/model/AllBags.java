package model;

public class AllBags {
	
	private PeopleBag peopleBag;
	private TextBookBag textbookBag;
	private MasterCourseBag masterCourseBag;
	private final int PEOPLEBAG_MAXSIZE = 100;
	private final int TEXTBOOKBAG_MAXSIZE = 100;
	private final int MASTERCOURSEBAG_MAXSIZE = 100;
	
	public AllBags() {
		peopleBag = new PeopleBag(PEOPLEBAG_MAXSIZE);
		textbookBag = new TextBookBag(TEXTBOOKBAG_MAXSIZE);
		masterCourseBag = new MasterCourseBag(MASTERCOURSEBAG_MAXSIZE);
	}

	public PeopleBag getPeopleBag() {
		return peopleBag;
	}

	public void setPeopleBag(PeopleBag peopleBag) {
		this.peopleBag = peopleBag;
	}

	public TextBookBag getTextbookBag() {
		return textbookBag;
	}

	public void setTextbookBag(TextBookBag textbookBag) {
		this.textbookBag = textbookBag;
	}
	public void setMasterCourseBag(MasterCourseBag masterCourseBag) {
		this.masterCourseBag = masterCourseBag;
	}
	public MasterCourseBag getMasterCourseBag() {
		return masterCourseBag;
	}
	public void save() {
		peopleBag.save();
		textbookBag.save();
		masterCourseBag.save();
	}
	public void load() {
		peopleBag.load();
		textbookBag.load();
		masterCourseBag.load();
	}
	
}
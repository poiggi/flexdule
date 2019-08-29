package flexdule.core.dtos;


import java.io.Serializable;


//algunos getters y setters directos de atributos, por referencia antinula ( es decir en caso de nulo la referencia es al hijo final)
public class Activity implements Serializable {
	private static final long serialVersionUID = 5042615882753137292L;
	
	private Integer idActivity;
	private Integer idSchedule;
	private String name;
	private String color;
	private Integer positionInSchedule;
	private ActivityVars configVars = new ActivityVars();
	private ActivityVars finalVars = new ActivityVars();
	private Limits limits = new Limits();

	public Activity() {
	}

	public Activity(Activity ac) {
		idActivity = ac.getIdActivity();
		idSchedule = ac.getIdSchedule();
		name = ac.getName();
		color = ac.getColor();
		positionInSchedule = ac.getPositionInSchedule();
		configVars = new ActivityVars(ac.getConfigVars());
		finalVars = new ActivityVars(ac.getFinalVars());
		limits = new Limits(ac.getLimits());
	}

	public Integer getIdActivity() {
		return idActivity;
	}

	public void setIdActivity(Integer idActivity) {
		this.idActivity = idActivity;
	}

	public Integer getIdSchedule() {
		return idSchedule;
	}

	public void setIdSchedule(Integer idSchedule) {
		this.idSchedule = idSchedule;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getPositionInSchedule() {
		return positionInSchedule;
	}

	public void setPositionInSchedule(Integer positionInSchedule) {
		this.positionInSchedule = positionInSchedule;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ActivityVars getConfigVars() {
		return configVars;
	}

	public void setConfigVars(ActivityVars configVars) {
		if (configVars != null) {
			this.configVars = configVars;
		} else {
			this.configVars.setS(null);
			this.configVars.setD(null);
			this.configVars.setF(null);
		}
	}

	public ActivityVars getFinalVars() {
		return finalVars;
	}

	public void setFinalVars(ActivityVars finalVars) {
		if (finalVars != null) {
			this.finalVars = finalVars;
		} else {
			this.finalVars.setS(null);
			this.finalVars.setD(null);
			this.finalVars.setF(null);
		}
	}

	public Limits getLimits() {
		return limits;
	}

	public void setLimits(Limits limits) {
		if (limits != null) {
			this.limits = limits;
		} else {
			this.limits.setS(null);
			this.limits.setF(null);
		}
	}

	@Override
	public String toString() {
		return "Activity [idActivity=" + idActivity + ", idSchedule=" + idSchedule + ", name=" + name + ", color="
				+ color + ", positionInSchedule=" + positionInSchedule + ", configVars=" + configVars + ", finalVars="
				+ finalVars + ", limits=" + limits + "]";
	}

}

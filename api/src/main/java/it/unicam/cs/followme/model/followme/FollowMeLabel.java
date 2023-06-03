package it.unicam.cs.followme.model.followme;

public record FollowMeLabel(String label) {
    public static final String LABEL_REGEX = "[a-zA-Z0-9_]*";
    public FollowMeLabel {
        if (!label.matches(LABEL_REGEX))
            throw new IllegalArgumentException(label+" is not a valid FollowMe label");
    }

}

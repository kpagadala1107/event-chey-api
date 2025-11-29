package com.kp.eventchey.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Poll {
    private String id;
    private String question;
    private List<String> options;
    private Map<String, Integer> votes;

    public Poll() {
        this.votes = new HashMap<>();
    }

    public Poll(String id, String question, List<String> options, Map<String, Integer> votes) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.votes = votes != null ? votes : new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Map<String, Integer> getVotes() {
        return votes;
    }

    public void setVotes(Map<String, Integer> votes) {
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poll poll = (Poll) o;
        return Objects.equals(id, poll.id) &&
                Objects.equals(question, poll.question) &&
                Objects.equals(options, poll.options) &&
                Objects.equals(votes, poll.votes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question, options, votes);
    }

    @Override
    public String toString() {
        return "Poll{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", options=" + options +
                ", votes=" + votes +
                '}';
    }
}


package jdo.duplicate.classes;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

import javax.jdo.annotations.Index;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DataRecord
{
    @Id
    private Long _id;
    @Index
    @ElementCollection
    private Set<Long> _children = new HashSet<>();

    public DataRecord()
    {
        // Empty constructor for JPA
    }

    public DataRecord(Long id, Set<Long> children)
    {
        _id = requireNonNull(id);
        _children = new HashSet<>(children);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DataRecord that = (DataRecord) o;
        return Objects.equals(_id, that._id) && Objects.equals(_children, that._children);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(_id, _children);
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ", DataRecord.class.getSimpleName() + "[", "]")
            .add("_id=" + _id)
            .add("_children=" + _children)
            .toString();
    }

    public Long getId()
    {
        return _id;
    }

    public Set<Long> getChildren()
    {
        return _children;
    }
}

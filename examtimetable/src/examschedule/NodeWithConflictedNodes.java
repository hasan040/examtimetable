package examschedule;

import java.util.List;

public class NodeWithConflictedNodes implements Comparable<NodeWithConflictedNodes>{
    private CourseDetails mSourceNode;
    private List<CourseDetails> mDestNodesList;

    public NodeWithConflictedNodes(CourseDetails mSourceNode, List<CourseDetails> mDestNodesList) {
        this.mSourceNode = mSourceNode;
        this.mDestNodesList = mDestNodesList;
    }

    public NodeWithConflictedNodes() {
    }

    public CourseDetails getmSourceNode() {
        return mSourceNode;
    }

    public void setmSourceNode(CourseDetails mSourceNode) {
        this.mSourceNode = mSourceNode;
    }

    public List<CourseDetails> getmDestNodesList() {
        return mDestNodesList;
    }

    public void setmDestNodesList(List<CourseDetails> mDestNodesList) {
        this.mDestNodesList = mDestNodesList;
    }

    @Override
    public int compareTo(NodeWithConflictedNodes o) {
        return this.getmDestNodesList().size() - o.getmDestNodesList().size();
    }
}

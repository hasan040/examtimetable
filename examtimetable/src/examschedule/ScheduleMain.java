package examschedule;

import java.io.*;
import java.util.*;

public class ScheduleMain {

    private  List<CourseWithTStudents> courseWithTStudentsList; //course with total students
    private  List<StRegisteredCourses> stRegisteredCoursesList;
    private  List<CourseDetails> courseDetailsList;

    private  List<NodeWithConflictedNodes> nodeWithConflictedNodesList;
    private  List<NodeWithConflictedNodes> nodeWithConflictedNodesListForRandom;
    private  List<NodeWithConflictedNodes> nodeWithConflictedNodesListForLargestDegree;



    private  void initComponents(String courseFilePath,String studentFilePath){

        courseWithTStudentsList = new ArrayList<>();
        stRegisteredCoursesList = new ArrayList<>();

        String currentDirectory = System.getProperty("user.dir");


        File courseFile = new File(currentDirectory,courseFilePath);
        File studentFile = new File(currentDirectory,studentFilePath);

        BufferedReader courseFileReader = null;
        BufferedReader studentFileReader = null;

        String courseLine;

        String registeredCoursesLine;

        try {
            courseFileReader = new BufferedReader(new FileReader(courseFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int line_counter = 0;


        while (true){
            try {
                if((courseLine = courseFileReader.readLine()) != null){
                    line_counter ++;

                    String tempString = courseLine.toString();
                    String [] tempSplit = tempString.split(" ");

                    int tempCourseID = Integer.parseInt(tempSplit[0]);
                    int tempEnrolledStudents = Integer.parseInt(tempSplit[1]);

                    courseWithTStudentsList.add(new CourseWithTStudents(tempCourseID,tempEnrolledStudents));



                }
                else{
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        int another_line_counter = 0;



        try {
            studentFileReader = new BufferedReader(new FileReader(studentFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (true){
            try {
                if((registeredCoursesLine = studentFileReader.readLine())!= null){
                    another_line_counter++;

                    List<Integer> tempCoursesList = new ArrayList<>();

                    String tempCoursesIndex = registeredCoursesLine.toString();
                    String [] splitCourses = tempCoursesIndex.split(" ");

                    for(String tempStringIndex : splitCourses){
                        int tempCourseID = Integer.parseInt(tempStringIndex);
                        tempCoursesList.add(tempCourseID);
                    }

                    stRegisteredCoursesList.add(new StRegisteredCourses(another_line_counter,tempCoursesList));


                }

                else {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private  void courseArrangement(){
        courseDetailsList = new ArrayList<>();
        int totalCourseCount = courseWithTStudentsList.size();

        for(int i=1;i<=totalCourseCount;i++){

            List<Integer> tempCoursesList = new ArrayList<>();

            for(StRegisteredCourses stRegisteredCourses : stRegisteredCoursesList){
                for(int j : stRegisteredCourses.getRegisteredCourses()){
                    if(i == j){
                        tempCoursesList.add(stRegisteredCourses.getStudentID());
                        break;
                    }
                }
            }

            courseDetailsList.add(new CourseDetails(i,tempCoursesList));
        }


    }

    private  List<Integer> getConflictedStudentList(CourseDetails courseDetails1 , CourseDetails courseDetails2){

        ArrayList<Integer> firstOne = new ArrayList<>(courseDetails1.getEnrolledStudentList());
        ArrayList<Integer> secondOne = new ArrayList<>(courseDetails2.getEnrolledStudentList());

        firstOne.retainAll(secondOne);

        return firstOne;

    }

    private  void buildConflictedPair(){

        nodeWithConflictedNodesList = new ArrayList<>();

        int totalLength = courseDetailsList.size();

        ArrayList<CourseDetails> [] arrayCourseDestList;

        arrayCourseDestList = new ArrayList[totalLength];

        for(int i=0;i<totalLength;i++){
            arrayCourseDestList[i] = new ArrayList<>();
        }



        for(int i=0;i<totalLength-1;i++){
            CourseDetails tempSourceNode = courseDetailsList.get(i);
            for(int j=i+1;j<totalLength;j++){
                CourseDetails tempDestNode = courseDetailsList.get(j);

                if(getConflictedStudentList(tempSourceNode,tempDestNode).size() > 0){
                    arrayCourseDestList[i].add(tempDestNode);
                    arrayCourseDestList[j].add(tempSourceNode);
                }

            }
        }

        for(int i=0;i<totalLength;i++){
            nodeWithConflictedNodesList.add(new NodeWithConflictedNodes(courseDetailsList.get(i),arrayCourseDestList[i]));
        }
    }

    private  void randomTimeSlotCreate(){

        int total_length = nodeWithConflictedNodesList.size();

        nodeWithConflictedNodesListForRandom = new ArrayList<>(nodeWithConflictedNodesList);

        Set<Integer> defined_time_slot_set_list = new HashSet<>();

        for(int i=0;i<total_length;i++){
            defined_time_slot_set_list.add(i+1);
        }
        for(int i=0;i<total_length;i++){
            NodeWithConflictedNodes mNodeWithConflictedNodes = nodeWithConflictedNodesListForRandom.get(i);
            if(mNodeWithConflictedNodes.getmDestNodesList().size() != 0){
                Set<Integer> get_dest_temp_slot_list = new HashSet<>();
                for(CourseDetails mCourseDetails : mNodeWithConflictedNodes.getmDestNodesList()){
                    int get_temp_time_slot = mCourseDetails.getTimeSlotId();
                    if(get_temp_time_slot != 9999){
                        get_dest_temp_slot_list.add(get_temp_time_slot);
                    }
                }
                int get_the_min_val = 1;
                if(get_dest_temp_slot_list.size() != 0){
                    Set<Integer> temp_slot_list = new HashSet<>(defined_time_slot_set_list);
                    temp_slot_list.removeAll(get_dest_temp_slot_list);
                    get_the_min_val = Collections.min(temp_slot_list);
                    mNodeWithConflictedNodes.getmSourceNode().setTimeSlotId(get_the_min_val);
                }
                else{
                    mNodeWithConflictedNodes.getmSourceNode().setTimeSlotId(get_the_min_val);
                }

                for(CourseDetails tempCourseList : mNodeWithConflictedNodes.getmDestNodesList()){
                    NodeWithConflictedNodes tempNode = nodeWithConflictedNodesListForRandom.get(tempCourseList.getCourseIndexInfo()-1);
                    for(CourseDetails courseDetails : tempNode.getmDestNodesList()){
                        if(courseDetails.getCourseIndexInfo() == mNodeWithConflictedNodes.getmSourceNode().getCourseIndexInfo()){
                            courseDetails.setTimeSlotId(get_the_min_val);
                            break;
                        }
                    }
                }


            }



        }

    }

    private List<NodeWithConflictedNodes> largestDegreeArrangement(){


        List<NodeWithConflictedNodes> tempConflictedNodesList;
        tempConflictedNodesList = new ArrayList<>();

        for(NodeWithConflictedNodes ncNodes : nodeWithConflictedNodesList){
            if(ncNodes.getmDestNodesList().size() > 0){
                tempConflictedNodesList.add(ncNodes);
            }
        }

        Collections.sort(tempConflictedNodesList,Collections.reverseOrder());

        return tempConflictedNodesList;

    }

    private void largestDegreeTimeSlotBuild(){

        nodeWithConflictedNodesListForLargestDegree = new ArrayList<>(nodeWithConflictedNodesList);

        Set<Integer> originalTimeSlotSet = new HashSet<>();

        int color_length = nodeWithConflictedNodesListForLargestDegree.size();

        for(int i=0;i<color_length;i++){
            originalTimeSlotSet.add(i+1);
        }

        List<NodeWithConflictedNodes> tempConflictedNodesList;
        tempConflictedNodesList = new ArrayList<>(largestDegreeArrangement());


        for(int i=0;i<tempConflictedNodesList.size();i++){
            int sourceNodeIndex = tempConflictedNodesList.get(i).getmSourceNode().getCourseIndexInfo();

            Set<Integer> starterTimeSlotSet = new HashSet<>(originalTimeSlotSet);

            Set<Integer> loopingTimeSlotSet = new HashSet<>();

            for(CourseDetails destCourse : nodeWithConflictedNodesListForLargestDegree.get(sourceNodeIndex-1).getmDestNodesList()){
                int getTimeSlotStatus = destCourse.getTimeSlotId();

                if(getTimeSlotStatus != 9999){
                    loopingTimeSlotSet.add(getTimeSlotStatus);
                }

            }

            if(loopingTimeSlotSet.size() > 0){
                starterTimeSlotSet.removeAll(loopingTimeSlotSet);
            }

            int minValStatus = Collections.min(starterTimeSlotSet);
            nodeWithConflictedNodesListForLargestDegree.get(sourceNodeIndex-1).getmSourceNode().setTimeSlotId(minValStatus);

            for(CourseDetails tempDestNode : nodeWithConflictedNodesListForLargestDegree.get(sourceNodeIndex-1).getmDestNodesList()){
                int destTrackIndex = tempDestNode.getCourseIndexInfo();
                for(CourseDetails innerSourceFinder : nodeWithConflictedNodesListForLargestDegree.get(destTrackIndex-1).getmDestNodesList()){
                    if(innerSourceFinder.getCourseIndexInfo() == sourceNodeIndex){
                        innerSourceFinder.setTimeSlotId(minValStatus);
                        break;
                    }
                }
            }

        }


    }

    private int showTotalTimeSlot(){

        Set<Integer> timeSlotList = new HashSet<>();

        for(NodeWithConflictedNodes nConflicted : nodeWithConflictedNodesListForRandom){

            int tempTimeSlot = nConflicted.getmSourceNode().getTimeSlotId();
            if(tempTimeSlot != 9999){
                timeSlotList.add(tempTimeSlot);
            }


            //System.out.println(nConflicted.getmSourceNode().getCourseIndexInfo()+" -> "+tempTimeSlot);

        }

        return Collections.max(timeSlotList);

    }

    private int showTotalTimeSlotForLargestDegree(){

        Set<Integer> timeSlotList = new HashSet<>();
        for(NodeWithConflictedNodes nConflicted : nodeWithConflictedNodesListForLargestDegree){

            int tempTimeSlot = nConflicted.getmSourceNode().getTimeSlotId();
            if(tempTimeSlot != 9999){
                timeSlotList.add(tempTimeSlot);
            }
            //System.out.println(nConflicted.getmSourceNode().getCourseIndexInfo()+" -> "+tempTimeSlot);
        }
        return Collections.max(timeSlotList);
    }

    private  void penaltyCountForStudents(){

        List<Integer> trackTimeSlot;
        List<Integer> calTimeSlotDistance;


        for(StRegisteredCourses tempStudentWithCourse : stRegisteredCoursesList){
            if(tempStudentWithCourse.getRegisteredCourses().size() > 1){
                trackTimeSlot = new ArrayList<>();

                for(int tempCourseIndex : tempStudentWithCourse.getRegisteredCourses()){
                    int tempCourseTimeSlot = nodeWithConflictedNodesListForRandom.get(tempCourseIndex-1).getmSourceNode().getTimeSlotId();

                    if(tempCourseTimeSlot != 9999){
                        trackTimeSlot.add(tempCourseTimeSlot);
                    }
                    else{
                        trackTimeSlot.add(1);
                    }

                }

                Collections.sort(trackTimeSlot);
                calTimeSlotDistance = new ArrayList<>();
                for(int i=0;i<trackTimeSlot.size()-1;i++){
                    int get_distance = trackTimeSlot.get(i+1) - trackTimeSlot.get(i);
                    calTimeSlotDistance.add(get_distance);
                }

                int total_penalty_gained = 0;

                for(int x : calTimeSlotDistance){
                    if(x == 1){
                        total_penalty_gained += 16;
                    }
                    else if(x == 2){
                        total_penalty_gained += 8;
                    }
                    else if(x == 3){
                        total_penalty_gained += 4;
                    }
                    else if(x == 4){
                        total_penalty_gained += 2;
                    }
                    else if(x == 5){
                        total_penalty_gained += 1;
                    }
                    else {
                        total_penalty_gained += 0;
                    }
                }

                tempStudentWithCourse.setTotalPenalty(total_penalty_gained);

            }
        }
    }

    private void penaltyCountForStudentsLargestDegree(){


        List<Integer> trackTimeSlot;
        List<Integer> calTimeSlotDistance;


        for(StRegisteredCourses tempStudentWithCourse : stRegisteredCoursesList){
            if(tempStudentWithCourse.getRegisteredCourses().size() > 1){
                trackTimeSlot = new ArrayList<>();

                for(int tempCourseIndex : tempStudentWithCourse.getRegisteredCourses()){
                    int tempCourseTimeSlot = nodeWithConflictedNodesListForLargestDegree.get(tempCourseIndex-1).getmSourceNode().getTimeSlotId();

                    if(tempCourseTimeSlot != 9999){
                        trackTimeSlot.add(tempCourseTimeSlot);
                    }
                    else{
                        trackTimeSlot.add(1);
                    }

                }

                Collections.sort(trackTimeSlot);
                calTimeSlotDistance = new ArrayList<>();
                for(int i=0;i<trackTimeSlot.size()-1;i++){
                    int get_distance = trackTimeSlot.get(i+1) - trackTimeSlot.get(i);
                    calTimeSlotDistance.add(get_distance);
                }

                int total_penalty_gained = 0;

                for(int x : calTimeSlotDistance){
                    if(x == 1){
                        total_penalty_gained += 16;
                    }
                    else if(x == 2){
                        total_penalty_gained += 8;
                    }
                    else if(x == 3){
                        total_penalty_gained += 4;
                    }
                    else if(x == 4){
                        total_penalty_gained += 2;
                    }
                    else if(x == 5){
                        total_penalty_gained += 1;
                    }
                    else {
                        total_penalty_gained += 0;
                    }
                }

                tempStudentWithCourse.setTotalPenalty(total_penalty_gained);

            }
        }
    }

    private float calculateAvgPenalty(){


        float counting_total = 0;

        for(StRegisteredCourses st : stRegisteredCoursesList){
            counting_total += st.getTotalPenalty();
            //System.out.println(st.getStudentID()+" --> "+st.getTotalPenalty());
        }

        return (counting_total / stRegisteredCoursesList.size());



    }

    private void applyKChain(){

        List<NodeWithConflictedNodes> tempLargestDegreeFirstList;
        tempLargestDegreeFirstList = new ArrayList<>(largestDegreeArrangement());

        List<NodeWithConflictedNodes> mKempChainList;
        mKempChainList = new ArrayList<>(nodeWithConflictedNodesListForLargestDegree);

        List<NodeWithConflictedNodes> kempChainToBeBuilt;
        kempChainToBeBuilt = new ArrayList<>();

        for(int i=tempLargestDegreeFirstList.size()-1;i>tempLargestDegreeFirstList.size()-2;i--){
            int tempSourceIndex = tempLargestDegreeFirstList.get(i).getmSourceNode().getCourseIndexInfo();
            int tempSourceIndexDestListLength = tempLargestDegreeFirstList.get(i).getmDestNodesList().size();
            if(tempSourceIndexDestListLength > 0){

                NodeWithConflictedNodes firstNode = mKempChainList.get(tempSourceIndex-1);
                NodeWithConflictedNodes secondNode = mKempChainList.get(firstNode.getmDestNodesList().get(0).getCourseIndexInfo()-1);

                int firstNodeTimeSlot = firstNode.getmSourceNode().getTimeSlotId();

                kempChainToBeBuilt.add(firstNode);
                kempChainToBeBuilt.add(secondNode);

                for(CourseDetails tempCourse : secondNode.getmDestNodesList()){
                    int trackTheTimeSlot = tempCourse.getTimeSlotId();
                    if(trackTheTimeSlot == firstNodeTimeSlot && tempCourse.getCourseIndexInfo() != firstNode.getmSourceNode().getCourseIndexInfo()){
                        kempChainToBeBuilt.add(mKempChainList.get(tempCourse.getCourseIndexInfo()-1));
                        break;
                    }
                }

                while (true){

                    int currentSize = kempChainToBeBuilt.size();

                    if(currentSize == 2){
                        break;
                    }

                    if(kempChainToBeBuilt.get(0).getmSourceNode().getCourseIndexInfo() == kempChainToBeBuilt.get(kempChainToBeBuilt.size()-1).getmSourceNode().getCourseIndexInfo()){
                        break;
                    }

                    NodeWithConflictedNodes sourceToBeMatched = kempChainToBeBuilt.get(kempChainToBeBuilt.size()-2);
                    int sourceTimeSlot = sourceToBeMatched.getmSourceNode().getTimeSlotId();
                    NodeWithConflictedNodes targetNodeList = kempChainToBeBuilt.get(kempChainToBeBuilt.size()-1);

                    for(CourseDetails tm : targetNodeList.getmDestNodesList()){
                        if(sourceTimeSlot == tm.getTimeSlotId() && tm.getCourseIndexInfo() !=sourceToBeMatched.getmSourceNode().getCourseIndexInfo()){

                            boolean previous_existence = false;

                            for(int j=1;j<kempChainToBeBuilt.size();j++){
                                if(tm.getCourseIndexInfo() == kempChainToBeBuilt.get(j).getmSourceNode().getCourseIndexInfo()){
                                    previous_existence = true;
                                    break;

                                }
                            }

                            if(!previous_existence){
                                kempChainToBeBuilt.add(mKempChainList.get(tm.getCourseIndexInfo()-1));
                            }

                            break;
                        }
                    }

                    int final_size = kempChainToBeBuilt.size();
                    if(currentSize == final_size){
                        break;
                    }
                }
            }
        }

        int builtLengthKC = kempChainToBeBuilt.size();
        if(builtLengthKC >= 3 ){
            int firstNodeTimeSlot = kempChainToBeBuilt.get(0).getmSourceNode().getTimeSlotId();
            int secondNodeTimeSlot = kempChainToBeBuilt.get(1).getmSourceNode().getTimeSlotId();

            for(int i=0;i<builtLengthKC;i++){
                if(i%2 == 0){
                    mKempChainList.get(kempChainToBeBuilt.get(i).getmSourceNode().getCourseIndexInfo()-1).getmSourceNode().setTimeSlotId(secondNodeTimeSlot);
                }
                else{
                    mKempChainList.get(kempChainToBeBuilt.get(i).getmSourceNode().getCourseIndexInfo()-1).getmSourceNode().setTimeSlotId(firstNodeTimeSlot);
                }
            }
        }

        penaltyCountApplyKempChain(mKempChainList);



    }

    private void penaltyCountApplyKempChain(List<NodeWithConflictedNodes> mKempChainList){



        List<Integer> trackTimeSlot;
        List<Integer> calTimeSlotDistance;


        for(StRegisteredCourses tempStudentWithCourse : stRegisteredCoursesList){
            if(tempStudentWithCourse.getRegisteredCourses().size() > 1){
                trackTimeSlot = new ArrayList<>();

                for(int tempCourseIndex : tempStudentWithCourse.getRegisteredCourses()){
                    int tempCourseTimeSlot = mKempChainList.get(tempCourseIndex-1).getmSourceNode().getTimeSlotId();

                    if(tempCourseTimeSlot != 9999){
                        trackTimeSlot.add(tempCourseTimeSlot);
                    }
                    else{
                        trackTimeSlot.add(1);
                    }

                }

                Collections.sort(trackTimeSlot);
                calTimeSlotDistance = new ArrayList<>();
                for(int i=0;i<trackTimeSlot.size()-1;i++){
                    int get_distance = trackTimeSlot.get(i+1) - trackTimeSlot.get(i);
                    calTimeSlotDistance.add(get_distance);
                }

                int total_penalty_gained = 0;

                for(int x : calTimeSlotDistance){
                    if(x == 1){
                        total_penalty_gained += 16;
                    }
                    else if(x == 2){
                        total_penalty_gained += 8;
                    }
                    else if(x == 3){
                        total_penalty_gained += 4;
                    }
                    else if(x == 4){
                        total_penalty_gained += 2;
                    }
                    else if(x == 5){
                        total_penalty_gained += 1;
                    }
                    else {
                        total_penalty_gained += 0;
                    }
                }

                tempStudentWithCourse.setTotalPenalty(total_penalty_gained);

            }
        }

    }


    public static void main(String[] args) {

        String yor83CourseFilePath = "/src/yor83courseFile.txt";
        String yor83StudentFilePath = "/src/yor83studentFile.txt";

        String car91CourseFilePath = "/src/car91courseFile.txt";
        String car91StudentFilePath = "/src/car91studentFile.txt";

        String car92CourseFilePath = "/src/car92courseFile.txt";
        String car92StudentFilePath = "/src/car92studentFile.txt";

        String kfu93CourseFilePath = "/src/kfu93courseFile.txt";
        String kfu93StudentFilePath = "/src/kfu93studentFile.txt";

        String tre92CourseFilePath = "/src/tre92courseFile.txt";
        String tre92StudentFilePath = "/src/tre92studentFile.txt";


        //-------------------------------------------------

        System.out.println("SCENARIO-1");


        ScheduleMain schYor83 = new ScheduleMain();
        schYor83.initComponents(yor83CourseFilePath,yor83StudentFilePath);
        schYor83.courseArrangement();
        schYor83.buildConflictedPair();
        schYor83.randomTimeSlotCreate();
        schYor83.penaltyCountForStudents();
        System.out.println("Total Time Slot yor83: "+schYor83.showTotalTimeSlot());
        System.out.println("Avg Penalty Got yor83: "+schYor83.calculateAvgPenalty());
        System.out.println();



        ScheduleMain schCar91 = new ScheduleMain();
        schCar91.initComponents(car91CourseFilePath,car91StudentFilePath);
        schCar91.courseArrangement();
        schCar91.buildConflictedPair();
        schCar91.randomTimeSlotCreate();
        schCar91.penaltyCountForStudents();
        System.out.println("Total Time Slot Count car91: "+schCar91.showTotalTimeSlot());
        System.out.println("Avg Penalty Got car91: "+schCar91.calculateAvgPenalty());
        System.out.println();



        ScheduleMain schCar92 = new ScheduleMain();
        schCar92.initComponents(car92CourseFilePath,car92StudentFilePath);
        schCar92.courseArrangement();
        schCar92.buildConflictedPair();
        schCar92.randomTimeSlotCreate();
        schCar92.penaltyCountForStudents();
        System.out.println("Total Time Slot Count car92: "+schCar92.showTotalTimeSlot());
        System.out.println("Avg Penalty Got car92: "+schCar92.calculateAvgPenalty());
        System.out.println();



        ScheduleMain schKfu93 = new ScheduleMain();
        schKfu93.initComponents(kfu93CourseFilePath,kfu93StudentFilePath);
        schKfu93.courseArrangement();
        schKfu93.buildConflictedPair();
        schKfu93.randomTimeSlotCreate();
        schKfu93.penaltyCountForStudents();
        System.out.println("Total Time Slot Count kfu93: "+schKfu93.showTotalTimeSlot());
        System.out.println("Avg Penalty Got kfu93: "+schKfu93.calculateAvgPenalty());
        System.out.println();



        ScheduleMain schTre92 = new ScheduleMain();
        schTre92.initComponents(tre92CourseFilePath,tre92StudentFilePath);
        schTre92.courseArrangement();
        schTre92.buildConflictedPair();
        schTre92.randomTimeSlotCreate();
        schTre92.penaltyCountForStudents();
        System.out.println("Total Time Slot Count tre92: "+schTre92.showTotalTimeSlot());
        System.out.println("Avg Penalty Got tre92: "+schTre92.calculateAvgPenalty());
        System.out.println();




        //-------------------------------------------------------


        System.out.println();
        System.out.println("SCENARIO-2");

        ScheduleMain schYor83LargestE = new ScheduleMain();
        schYor83LargestE.initComponents(yor83CourseFilePath,yor83StudentFilePath);
        schYor83LargestE.courseArrangement();
        schYor83LargestE.buildConflictedPair();
        schYor83LargestE.largestDegreeTimeSlotBuild();
        System.out.println("total time slot (yor83): "+schYor83LargestE.showTotalTimeSlotForLargestDegree());
        schYor83LargestE.penaltyCountForStudentsLargestDegree();
        System.out.println("avg penalty : "+schYor83LargestE.calculateAvgPenalty());
        schYor83LargestE.applyKChain();
        System.out.println("applying kemp chain :"+schYor83LargestE.calculateAvgPenalty());
        System.out.println();


        ScheduleMain schCar91LargestE = new ScheduleMain();
        schCar91LargestE.initComponents(car91CourseFilePath,car91StudentFilePath);
        schCar91LargestE.courseArrangement();
        schCar91LargestE.buildConflictedPair();
        schCar91LargestE.largestDegreeTimeSlotBuild();
        System.out.println("total time slot (car91): "+schCar91LargestE.showTotalTimeSlotForLargestDegree());
        schCar91LargestE.penaltyCountForStudentsLargestDegree();
        System.out.println("avg penalty : "+schCar91LargestE.calculateAvgPenalty());
        schYor83LargestE.applyKChain();
        System.out.println("applying kemp chain :"+schCar91LargestE.calculateAvgPenalty());
        System.out.println();


        ScheduleMain schCar92LargestE = new ScheduleMain();
        schCar92LargestE.initComponents(car92CourseFilePath,car92StudentFilePath);
        schCar92LargestE.courseArrangement();
        schCar92LargestE.buildConflictedPair();
        schCar92LargestE.largestDegreeTimeSlotBuild();
        System.out.println("total time slot (car92): "+schCar92LargestE.showTotalTimeSlotForLargestDegree());
        schCar92LargestE.penaltyCountForStudentsLargestDegree();
        System.out.println("avg penalty : "+schCar92LargestE.calculateAvgPenalty());
        schYor83LargestE.applyKChain();
        System.out.println("applying kemp chain :"+schCar92LargestE.calculateAvgPenalty());
        System.out.println();





        ScheduleMain schTre92LargestE = new ScheduleMain();
        schTre92LargestE.initComponents(tre92CourseFilePath,tre92StudentFilePath);
        schTre92LargestE.courseArrangement();
        schTre92LargestE.buildConflictedPair();
        schTre92LargestE.largestDegreeTimeSlotBuild();
        System.out.println("total time slot (tre92): "+schTre92LargestE.showTotalTimeSlotForLargestDegree());
        schTre92LargestE.penaltyCountForStudentsLargestDegree();
        System.out.println("avg penalty : "+schTre92LargestE.calculateAvgPenalty());
        schTre92LargestE.applyKChain();
        System.out.println("applying kemp chain :"+schTre92LargestE.calculateAvgPenalty());
        System.out.println();

        ScheduleMain schKfu93LargestE = new ScheduleMain();
        schKfu93LargestE.initComponents(kfu93CourseFilePath,kfu93StudentFilePath);
        schKfu93LargestE.courseArrangement();
        schKfu93LargestE.buildConflictedPair();
        schKfu93LargestE.largestDegreeTimeSlotBuild();
        System.out.println("total time slot (kfu93): "+schKfu93LargestE.showTotalTimeSlotForLargestDegree());
        schKfu93LargestE.penaltyCountForStudentsLargestDegree();
        System.out.println("avg penalty : "+schKfu93LargestE.calculateAvgPenalty());
        schYor83LargestE.applyKChain();
        System.out.println("applying kemp chain :"+schKfu93LargestE.calculateAvgPenalty());
        System.out.println();



















    }
}

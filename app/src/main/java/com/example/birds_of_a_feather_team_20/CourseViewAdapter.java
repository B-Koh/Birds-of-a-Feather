//package com.example.birds_of_a_feather_team_20;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.birds_of_a_feather_team_20.model.db.Course;
//
//import java.util.List;
//import java.util.function.Consumer;
//
//public class CoursesViewAdapter extends RecyclerView.Adapter<CoursesViewAdapter.ViewHolder> {
//    private final List<Course> courses;
//    private final Consumer<Course> onCourseRemoved;
//
//    public CoursesViewAdapter(List<Course> courses, Consumer<Course> onCourseRemoved) {
//        super();
//        this.courses = courses;
//        this.onCourseRemoved = onCourseRemoved;
//    }
//
//    @NonNull
//    @Override
//    public CoursesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater
//                .from(parent.getContext())
//                .inflate(R.layout.notes_row, parent, false);
//
//        return new ViewHolder(view, this::removeNote, onCourseRemoved);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CoursesViewAdapter.ViewHolder holder, int position) {
//        holder.setCourse(courses.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return this.courses.size();
//    }
//
////    public void addNote(Course course){
////        this.courses.add(course);
////        this.notifyItemInserted(this.courses.size() - 1);
////    }
////
////    public void removeCourse(int position){
////        this.courses.remove(position);
////        this.notifyItemRemoved(position);
////    }
//
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        private final TextView courseTextView;
//        private Course course;
//
//        ViewHolder(View itemView, Consumer<Integer> removeCourse, Consumer<Course> onCourseRemoved) {
//            super(itemView);
//            this.courseTextView = itemView.findViewById(R.id.notes_row_text);
//
////            Button removeButton = itemView.findViewById(R.id.remove_note_button);
////            removeButton.setOnClickListener((view) -> {
////                removeCourse.accept(this.getAdapterPosition());
////                onCourseRemoved.accept(course);
////            });
//
//        }
//
//        public void setCourse(Course course) {
//            this.course = course;
//            this.courseTextView.setText(course.getDepartment());
//        }
//
//    }
//
//}
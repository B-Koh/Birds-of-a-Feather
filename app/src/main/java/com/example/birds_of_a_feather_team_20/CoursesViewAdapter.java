package com.example.birds_of_a_feather_team_20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.model.db.CourseDatabase;

import java.util.List;
import java.util.function.Consumer;

public class CoursesViewAdapter extends RecyclerView.Adapter<CoursesViewAdapter.ViewHolder> {
    private List<Course> courses;
    private final Context context;
    private final boolean isMyProfile;

    public CoursesViewAdapter(List<Course> courses, Context context, boolean isMyProfile) {
        super();
        this.courses = courses;
        this.context = context;
        this.isMyProfile = isMyProfile;
    }

    @NonNull
    @Override
    public CoursesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.courses_row, parent, false);

        return new ViewHolder(view, context, this, isMyProfile);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesViewAdapter.ViewHolder holder, int position) {
        holder.setCourse(courses.get(position));
    }

    @Override
    public int getItemCount() {
        return this.courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseDepartment;
        private final TextView courseNumber;
        private final TextView courseSession;
        private final TextView courseYear;
        private final Button deleteButton;
        private final Context context;
        private final CoursesViewAdapter adapter;

        private Course course;

        ViewHolder(View itemView, Context context, CoursesViewAdapter adapter, boolean isMyProfile) {
            super(itemView);
            this.courseDepartment = itemView.findViewById(R.id.course_department_textview);
            this.courseNumber = itemView.findViewById(R.id.course_number_textview);
            this.courseSession = itemView.findViewById(R.id.course_session_textview);
            this.courseYear = itemView.findViewById(R.id.course_year_textview);
            deleteButton = itemView.findViewById(R.id.delete_course_button);
            if (!isMyProfile)
                deleteButton.setVisibility(View.GONE);
            this.context = context;
            this.adapter = adapter;
        }

        public void setCourse(Course course) {
            this.course = course;
            this.courseDepartment.setText(course.getDepartment());
            this.courseNumber.setText(course.getCourseNumber());
            this.courseSession.setText(course.getSession());
            this.courseYear.setText(String.valueOf(course.getYear()));
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CourseDatabase db = CourseDatabase.singleton(context);
                    int index = MyProfile.singleton(context).getCourses().indexOf(course);
                    if (index == -1) return;

                    if(db.courseDao().get(course.getYear(), course.getSession(), course.getDepartment(), course.getCourseNumber()) != null) {
                        db.courseDao().delete(db.courseDao().get(course.getYear(), course.getSession(), course.getDepartment(), course.getCourseNumber()));
                    }

                    MyProfile.singleton(context).setMyCourses(db.courseDao().getAll());
                    adapter.courses = MyProfile.singleton(context).getCourses();
                    adapter.notifyItemRemoved(index);
                }
            });
        }

    }
}
package ir.coursio.notes.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import ir.coursio.notes.R;
import ir.coursio.notes.model.NoteModel;
import ir.coursio.notes.presenter.NotePresenter;
import ir.coursio.notes.presenter.NotesListPresenter;
import ir.coursio.notes.view.custom.fab.FloatingActionButton;
import ir.coursio.notes.view.list.NotesListFragment;

/**
 * Created by Taher on 29/05/2017.
 * Project: notes
 */
@SuppressLint("ViewConstructor")
public class NoteView extends FrameLayout implements View.OnClickListener {

    ViewGroup mainLayout;
    FragmentManager fragmentManager;
    NotePresenter presenter;

    private enum ClickedItemType {TEXT, DRAWING}

    public NoteView(@NonNull Activity activity) {
        super(activity);
        View view = inflate(getContext(), R.layout.activity_note, this);

        mainLayout = (ViewGroup) view.findViewById(R.id.mainLayout);

        //FloatingActionButtons setup
        FloatingActionButton fabAddText = (FloatingActionButton) view.findViewById(R.id.fabAddText);
        FloatingActionButton fabAddDrawing = (FloatingActionButton) view.findViewById(R.id.fabAddDrawing);
        fabAddText.setTag(ClickedItemType.TEXT);
        fabAddDrawing.setTag(ClickedItemType.DRAWING);
        fabAddDrawing.setOnClickListener(this);
        fabAddText.setOnClickListener(this);

        //Notes list setup
        fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
        LoaderManager loaderManager = ((FragmentActivity) activity).getSupportLoaderManager();

        NotesListFragment notesList = (NotesListFragment) fragmentManager.findFragmentByTag("NotesListFragment");
        if (notesList == null) {
            notesList = new NotesListFragment();
            fragmentManager.beginTransaction().
                    add(R.id.mainLayout, notesList, "NotesFragment").commitAllowingStateLoss();
        }
        new NotesListPresenter(notesList, loaderManager, activity.getIntent().getStringExtra(NoteModel.FOLDER_ID));
    }

    public void setPresenter(NotePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onClick(View v) {
        switch ((ClickedItemType) v.getTag()) {

            //Create new Note
            case TEXT:
                ((OnNewNoteRequestListener) presenter).onNewNoteRequest();
                break;

            //Create new Drawing
            case DRAWING:
                Toast.makeText(getContext(), "Drawing", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * This interface request presenter to make or update a note.
     */
    public interface OnNewNoteRequestListener {
        void onNewNoteRequest();
    }
}

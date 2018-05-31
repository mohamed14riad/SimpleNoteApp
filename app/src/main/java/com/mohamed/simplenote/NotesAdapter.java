package com.mohamed.simplenote;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private Context context = null;
    private List<Note> notes = null;
    private ListClickListener listClickListener = null;

    // IDs for ContextMenu items
    private static final int MENU_ITEM_OPEN = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_DELETE = 333;

    private static int position;
    private String contextMenuTitle = null;

    public interface ListClickListener {
        void onItemClick(int position);
    }

    public NotesAdapter(Context context, List<Note> notes, ListClickListener listClickListener) {
        this.context = context;
        this.notes = notes;
        this.listClickListener = listClickListener;
    }

    public void addNewList(List<Note> notes) {
        if (notes != null) {
            this.notes.clear();
            this.notes.addAll(notes);
            notifyDataSetChanged();
        }
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, final int position) {
        final Note note = notes.get(position);
        holder.item_title.setText(note.getNoteTitle());
        holder.item_date.setText(note.getNoteDate());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                contextMenuTitle = note.getNoteTitle();
                return false;
            }
        });
    }

    @Override
    public void onViewRecycled(NoteViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        if (notes != null) {
            return notes.size();
        }
        return 0;
    }

    public static int getPosition() {
        return position;
    }

    private void setPosition(int position) {
        this.position = position;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        private TextView item_title = null;
        private TextView item_date = null;

        public NoteViewHolder(View itemView) {
            super(itemView);
            item_title = (TextView) itemView.findViewById(R.id.item_title);
            item_date = (TextView) itemView.findViewById(R.id.item_date);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            listClickListener.onItemClick(getLayoutPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
            if (contextMenuTitle != null && !contextMenuTitle.isEmpty()) {
                menu.setHeaderTitle(contextMenuTitle);
            } else {
                menu.setHeaderTitle("Select Action");
            }

            // groupId, itemId, itemOrder, itemTitle
            menu.add(Menu.NONE, MENU_ITEM_OPEN, 0, "Open");
            menu.add(Menu.NONE, MENU_ITEM_EDIT, 1, "Edit");
            menu.add(Menu.NONE, MENU_ITEM_DELETE, 2, "Delete");
        }
    }
}

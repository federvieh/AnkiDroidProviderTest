/*
 * Copying and distribution of this file, with or without modification, are permitted in any
 * medium without royalty. This file is offered as-is, without any warranty.
 */

package com.github.federvieh.ankidroidprovidertest;

import android.net.Uri;

/**
 * <p>
 * The contract between AnkiDroid and applications. Contains definitions for the supported URIs and
 * columns.
 * </p>
 * <h3>Overview</h3>
 * <p>
 * FlashCardsContract defines the access to flash card related information. Flash cards consist of:
 * <ul>
 *     <li>Fields: Fields contain the actual information of the flashcard that is used for learning.
 *         Typical fields are "Japanese" and "English" (for a native English speaker to learn
 *         Japanese), or just "front" and "back" (for a generic front side and back side of a card,
 *         without saying anything about the purpose). Fields can be accessed either directly
 *         through the {@link #CONTENT_URI} in the {@link FlashCard#FLDS} column or through the
 *         {@link FlashCardsContract.Data} content provider using a special
 *         {@link FlashCardsContract.Data.Field#MIMETYPE} for fields.</li>
 *     <li>Tags: Flash cards can be tagged. Tags can be accessed directly through the
 *         {@link #CONTENT_URI} in the {@link FlashCard#TAGS} column. Tags are separated  by spaces.
 * </ul>
 * Flash card information is provided in a two-tier data model:
 * </p>
 * <ul>
 * <li>
 * Each row from the {@link FlashCard} provider represents a note that is stored in AnkiDroid.
 * This provider must be used in order to find flashcards. Some of the data that is returned by
 * this provider can also be obtained through the {@link Data} in a more compact way. The flash
 * cards can be accessed by
 * the {@link #CONTENT_URI}.
 * </li>
 * <li>
 * A row from the {@link Data} provider gives access to flashcard related data, such
 * as the question of the flashcard or which deck it belongs to. To access this data the
 * </li>
 * </ul>
 */
public class FlashCardsContract {
    public static final String AUTHORITY = "com.ichi2.anki.flashcards";

    /** A content:// style uri to the authority for the flash card provider */
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * The content:// style URI for the flashcards. If the it is appended by the flash card's ID, this
     * flash card can be directly accessed, e.g.
     * <p>
     *     <pre>
     *         Uri noteUri = Uri.withAppendedPath(FlashCardsContract.CONTENT_URI, Long.toString(noteId));
     *     </pre>
     * </p>
     * If no ID is appended the content provider functions return
     * all the notes that match the query as defined in {@code selection} argument in the
     * {@code query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)} or
     * {@code update(Uri uri, ContentValues values, String selection, String[] selectionArgs)} call.
     * The {@code selectionArgs} parameter is always ignored. The query syntax that must go in the
     * {@code selection} argument is described here:
     * TODO: insert URL
     *
     * <p>
     * If the URI is appended by the flashcard ID and then the keyword "data", it is possible to
     * access the details of a card:
     * <p>
     *     <pre>
     *         Uri noteUri = Uri.withAppendedPath(FlashCardsContract.CONTENT_URI, Long.toString(noteId));
     *         Uri dataUri = Uri.withAppendedPath(noteUri, "data");
     *     </pre>
     * </p>
     * </p>
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "flashcards");

    /* Don't create instances of this class. */
    private FlashCardsContract(){};

    /** The flash cards can be accessed by
     * the {@link #CONTENT_URI}. If the {@link #CONTENT_URI} is appended by the flash card's ID, this
     * flash card can be directly accessed. If no ID is appended the content provides functions return
     * all the notes that match the query as defined in {@code selection} argument in the
     * {@code query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)} or
     * {@code update(Uri uri, ContentValues values, String selection, String[] selectionArgs)} call.
     * The {@code selectionArgs} parameter is always ignored. The query syntax that must go in the
     * {@code selection} argument is described here:
     * TODO: insert URL
     *
     * A flash card consists of the following columns:
     *
     * <table class="jd-sumtable">
     * <tr>
     * <th>Type</th><th>Name</th><th>access</th><th>description</th>
     * </tr>
     * <tr>
     * <td>long</td>
     * <td>{@link #_ID}</td>
     * <td>read-only</td>
     * <td>Row ID. This is the ID of the flash card. It is the same as the note ID in Anki. This
     * ID can be used for accessing the data of a flashcard using the URI
     * "content://com.ichi2.anki.flashcards/flashcards/&lt;ID&gt;/data</td>
     * </tr>
     * <tr>
     * <td>long</td>
     * <td>{@link #GUID}</td>
     * <td>read-write</td>
     * <td>??? TODO: This needs description. What is this?</td>
     * </tr>
     * <tr>
     * <td>long</td>
     * <td>{@link #MID}</td>
     * <td>read-only</td>
     * <td>This is the ID of the model that is used for rendering the cards. This ID can be used for
     * accessing the data of the model using the URI
     * "content://com.ichi2.anki.flashcards/model/&lt;ID&gt;</td>
     * </tr>
     * <tr>
     * <td>long</td>
     * <td>{@link #MOD}</td>
     * <td>read-only</td>
     * <td>??? TODO: This needs description. What is this?</td>
     * </tr>
     * <tr>
     * <td>long</td>
     * <td>{@link #USN}</td>
     * <td>read-only</td>
     * <td>??? TODO: This needs description. What is this?</td>
     * </tr>
     * <tr>
     * <td>long</td>
     * <td>{@link #TAGS}</td>
     * <td>read-write</td>
     * <td>Tags of this flash card. Tags are separated  by spaces.</td>
     * </tr>
     * <tr>
     * <td>String</td>
     * <td>{@link #FLDS}</td>
     * <td>read-only</td>
     * <td>Fields of this flash card.</td>
     * </tr>
     * <tr>
     * <td>long</td>
     * <td>{@link #SFLD}</td>
     * <td>read-only</td>
     * <td>??? TODO: This needs description. But what is this?</td>
     * </tr>
     * <tr>
     * <td>long</td>
     * <td>{@link #CSUM}</td>
     * <td>read-only</td>
     * <td>??? TODO: This needs description. But what is this? A checksum?</td>
     * </tr>
     * <tr>
     * <td>long</td>
     * <td>{@link #FLAGS}</td>
     * <td>read-only</td>
     * <td>??? TODO: This needs description. But what is this?</td>
     * </tr>
     * <tr>
     * <td>long</td>
     * <td>{@link #DATA}</td>
     * <td>read-only</td>
     * <td>??? TODO: This needs description. But what is this?</td>
     * </tr>
     */
    public class FlashCard{
        /** This is the ID of the flash card. It is the same as the note ID in Anki. This ID can be
         * used for accessing the data of a flashcard using the URI
         * "content://com.ichi2.anki.flashcards/flashcards/&lt;ID&gt;/data
         */
        public static final String _ID = "_id";
        public static final String GUID = "guid";
        public static final String MID = "mid";
        public static final String MOD = "mod";
        public static final String USN = "usn";
        public static final String TAGS = "tags";
        public static final String FLDS = "flds";
        public static final String SFLD = "sfld";
        public static final String CSUM = "csum";
        public static final String FLAGS = "flags";
        public static final String DATA = "data";
    }

    /** This is the generic interface class that describes the detailed content of flash cards.
     * The flash card details consist of four columns:
     *
     * <table class="jd-sumtable">
     * <tr>
     * <th>Type</th><th>Name</th><th>access</th><th>description</th>
     * </tr>
     * <tr>
     * <td>long</td>
     * <td>{@link #_ID}</td>
     * <td>read-only</td>
     * <td>Row ID. This is a virtual ID which actually does not exist in AnkiDroid's data base.
     * </tr>
     * <tr>
     * <td>long</td>
     * <td>{@link #NOTE_ID}</td>
     * <td>read-only</td>
     * <td>This is the ID of the flash card that this row belongs to (i.e. {@link FlashCard#_ID}).
     * </td>
     * </tr>
     * <tr>
     * <td>String</td>
     * <td>{@link #MIMETYPE}</td>
     * <td>read-only</td>
     * <td>This describes the MIME type of the row, which describes how to interpret the columns
     *     {@link #DATA1} and {@link #DATA2}.
     * </td>
     * </tr>
     * <tr>
     * <td>MIMETYPE dependent.</td>
     * <td>{@link #DATA1}</td>
     * <td>MIMETYPE dependent.</td>
     * <td>This is the first of two data columns. The column must be interpreted according to the
     *     {@link #MIMETYPE} column.
     * </td>
     * </tr>
     * <tr>
     * <td>long</td>
     * <td>{@link #DATA2}</td>
     * <td>MIMETYPE dependent.</td>
     * <td>This is the second of two data columns. The column must be interpreted according to the
     *     {@link #MIMETYPE} column.
     * </td>
     * </tr>
     * </table>
     */
    public interface DataColumns {
        /** Row ID. This is a virtual ID which actually does not exist in AnkiDroid's data base.
         * This column only exists so that this interface can be used with existing CursorAdapters
         * that require the existence of a "_id" column. This means, that it CAN NOT be used
         * reliably over subsequent queries. Especially if the number of cards or fields changes,
         * the _ID will change too.
         */
        public static final String _ID = "_id";

        /** This is the ID of the flash card that this row belongs to (i.e. {@link FlashCard#_ID}).
         */
        public static final String NOTE_ID = "note_id";

        /** This describes the MIME type of the row, which describes how to interpret the columns
         * {@link #DATA1} and {@link #DATA2}. Allowed values are:
         * <ul>
         *     <li>{@link FlashCardsContract.Data.Field#CONTENT_ITEM_TYPE}:
         *         You can use the aliases described in
         *         {@link FlashCardsContract.Data.Field} to access the
         *         columns instead of the generic "DATA1" or "DATA2".
         *     </li>
         *     <li>{@link FlashCardsContract.Data.Deck#CONTENT_ITEM_TYPE}:
         *         You can use the aliases described in
         *         {@link FlashCardsContract.Data.Deck} to access the
         *         columns instead of the generic "DATA1" or "DATA2".
         *     </li>
         * </ul>
         */
        public static final String MIMETYPE = "mimetype";

        /** This is the first of two data columns. The column must be interpreted according to the
         * {@link #MIMETYPE} column.
         */
        public static final String DATA1 = "data1";

        /** This is the second of two data columns. The column must be interpreted according to the
         * {@link #MIMETYPE} column.
         */
        public static final String DATA2 = "data2";
    }

    /** Container for definitions of common data types returned by the data content provider.
     */
    public class Data {

        /**  A data kind representing a field in a flash card.
         *
         * You can use the columns defined for
         * {@link FlashCardsContract.DataColumns} as well as the following
         * aliases.
         * <table class="jd-sumtable">
         * <tr>
         * <th>Type</th><th>Alias</th><th>Data column</th><th>access</th><th></th>
         * </tr>
         * <tr>
         *     <td>String</td>
         *     <td>{@link #FIELD_NAME}</td>
         *     <td>{@link FlashCardsContract.DataColumns#DATA1}</td>
         *     <td>read-only</td>
         *     <td>Field name</td>
         * </tr>
         * <tr>
         *     <td>String</td>
         *     <td>{@link #FIELD_CONTENT}</td>
         *     <td>{@link FlashCardsContract.DataColumns#DATA2}</td>
         *     <td>read-write</td>
         *     <td>Field content</td>
         * </tr>
         * </table>
         *
         * Since the fields are defined by the model type, it is not possible to insert or delete
         * fields. To update a field use the following code as an example: TODO
         */
        public class Field implements DataColumns {
            /** MIME type used for fields.
             */
            public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/flash_card_field";

            /** The field name as defined by the model, e.g. "front" or "back".
             */
            public static final String FIELD_NAME = DATA1;

            /** The content of the field, e.g. "dog" or "犬".
             */
            public static final String FIELD_CONTENT = DATA2;
        }

        /**   A data kind representing a field in a flash card.
         *
         * You can use the columns defined for
         * {@link FlashCardsContract.DataColumns} as well as the following
         * aliases.
         * <table class="jd-sumtable">
         * <tr>
         * <th>Type</th><th>Alias</th><th>Data column</th><th>access</th><th></th>
         * </tr>
         * <tr>
         *     <td>String</td>
         *     <td>{@link #CARD}</td>
         *     <td>{@link FlashCardsContract.DataColumns#DATA1}</td>
         *     <td>read-only</td>
         *     <td>Card name</td>
         * </tr>
         * <tr>
         *     <td>String</td>
         *     <td>{@link #DECK}</td>
         *     <td>{@link FlashCardsContract.DataColumns#DATA2}</td>
         *     <td>read-only</td>
         *     <td>Deck name</td>
         * </tr>
         * </table>
         *
         * Since the decks and cards are defined by AnkiDatabase it is not possible to update or
         * delete cards or decks. To insert a card into a new deck use the following code as an
         * example: TODO
         */
        public class Deck implements DataColumns {

            /** MIME type used for decks.
             */
            public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/flash_card_deck";

            /** The card name, e.g. "Card 1".
             */
            public static final String CARD = DATA1;

            /** The deck name, e.g. "Japanese for beginners".
             */
            public static final String DECK = DATA2;
        }
    }
}

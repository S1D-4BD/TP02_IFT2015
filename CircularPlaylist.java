package dv2;


import java.util.*;
import java.util.Random;


class Song {

    int id;
    String title;
    String artist;
    int duration;

    public Song(int id, String title, String artist, int duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }

    public String toString() {
        return id + ". " + title + " - " + artist + " (" + duration + " sec)";
    }
}

class CircularPlaylist {

    private Song[] playlist;
    private int front;
    private int rear;
    private int maxSize;
    private boolean shuffleMode;
    private boolean repeatMode;
    private int count;  // Keeps track of the number of songs

    public CircularPlaylist(int capacity) {
        this.maxSize = capacity;
        this.playlist = new Song[maxSize];
        this.front = 0;
        this.rear = 0;
        this.shuffleMode = false;
        this.repeatMode = false;
        this.count = 0;
    }

    /**
     * Ajout de la chanson a la playlist
     * @param song
     */
    public void addSong(Song song) {
        // Done
        if (count == maxSize) {
            throw new ArrayIndexOutOfBoundsException("Supprimer une chanson pour ajouter une autre.");
        } else {
            playlist[rear] = song;        // ajout la nouvelle chanson pour Respecter le principe FIFO
            rear = (rear + 1) % maxSize;  // incrementation de rear
            count++;
        }
    }
    /** Complexite Temporelle: addSong() ==> O(1)
     */

    public Song playSong() {
        //DONE
        if (count == 0) {
            throw new NoSuchElementException("Ajouter une chanson a la playlist pour l'écouter.");
        }

        if (repeatMode) {
            //  Mode répétition : on rejoue la même chanson sans avancer
            // On incremente pas front pour passe a la chansons suivante
            System.out.println("Lecture (mode repeat): " + playlist[front]);
            return playlist[front];
        }

        // Mode normal : jouer la chanson et avancer circulairement
        ////////////////////////////////////// ICII
        int chansonJouee = 0;

        while (playlist[front] == null){
            front = (front + 1) % maxSize;
        }

        Song currentSong = playlist[front]; // Sauvegarder la chanson actuelle
        System.out.println("Lecture : " + playlist[front]);
        front = (front + 1) % maxSize; // Passer à la chanson suivante

        return currentSong;
    }




    private int getRandomIndex() {
        Random rand = new Random();
        return (front + rand.nextInt(count)) % maxSize;
    }

    /**
     * Permet de passer a la chanson suivante.
     * Incremente front de facon circulaire en utilisant modulo.
     */
    public void nextSong() {
        // Done
        if (count == 0) {
            System.out.println("La playlist est vide ajouter des chansons.");
            return;
        }
        if (count > 0) {
            front = (front + 1) % maxSize;
        }
    }
    /** Complexite Temporelle: nextSong() ==>  O(1)
     * Des operation arithmetique simple.
     */



    /**
     * permet de revenir a la chanson precedente.
     */
    public void previousSong() {
        //Done
        if (count == 0) {
            throw new NoSuchElementException("La playlist est vide ajouter des chansons.");
        }
        if (count > 0) {
            // Ceci assure que front est tjrs positif tout en revenant en arriere d'une seule chanson
            front = (front - 1 + maxSize) % maxSize;
        }
    }
      /** Complexite Temporelle: previousSong() ==>   O(1)
        Des operation arithmetique simple.
       */


    /**
     * Permet de supprimer une chanson de la playlist par
     * son ID, tout en conservant le comportement de la file d’attente circulaire.
     *
     * Pour aller plus simple on va traiter des cas :
     * le cas ou la chanson est la premiere, le cas ou la chanson est la derniere
     * le cas ou la chanson est la seule et le cas ou la chanson est quelque part entre
     * @param id
     */
    public void removeSong(int id) {

        //CAS -> RIEN DANS LA PLAYLISTE
        if (count == 0) { //on peut rien faire avec une liste vide
            throw new NoSuchElementException("La playlist est vide!!");
        }

        int indexToRemove = -1;

        // Trouver la chanson à supprimer a partir de son id
        for (int i = 0; i < count; i++) {
            int actualIndex = (front + i) % maxSize; // Respect de la file circulaire
            if (playlist[actualIndex] != null && playlist[actualIndex].id == id) {
                indexToRemove = actualIndex;
                break;
            }
        }

        //ICI ->  id pas possible !!!!
        if (indexToRemove == -1) {
            throw new NoSuchElementException("Aucune chanson trouvée avec l'ID : " + id);
        }

        System.out.println("Suppression de : " + playlist[indexToRemove]);

        //A PARTIR D'ICI ON A L ID DE LA CHANSON A EFFACER

        //CAS ON SUPP LE PREMIER
        if (indexToRemove == front) { //on a besoin de faire 0 decalage, juste reculer le front
            playlist[front] = null; //eneleve l ancien
            front = (front + 1) % maxSize; // Avancer le front au next circulaire
        }

        //CAS ON SUPPRIME LE DERNIER ID RESTANT ET CA DEVIENT VIDE
        if (count == 1) { //il n en reste que 1
            playlist[front] = null; //la chanson pointee par front est null
            front = -1; //pointeur qui mene a un index impossible -> pour la liste vide
            rear = -1;
            count = 0; //VIDE
            System.out.println("Playlist vide maintenant");
            return;
        }

        //CAS ON SUPP LE DERNIER (PAS VIDE)
        else if (indexToRemove == rear) { //0 decalage, juste avancer lerear
            playlist[rear] = null;
            rear = (rear - 1 + maxSize) % maxSize; // on change le rear pour qui pointe la chanson d'avant
        }

        //CAS ON SUPPRIME ENTRE FRONT ET REAR  !! decalages obligatoire
        else {
            for (int i = indexToRemove; i != rear; i = (i + 1) % maxSize) { //ON Itere de l'id -> avant le rear pour tout decaler
                int nextIndex = (i + 1) % maxSize; //decalage cyclique
                playlist[i] = playlist[nextIndex]; //on va copier de la droite vers la gauche
            }
            playlist[rear] = null; //efface le rear actuel
            rear = (rear - 1 + maxSize) % maxSize; /// on change le rear pour qui pointe la chanson d'avant
        }

        count--; //la liste va diminuer de taille si on traite pas une liste vide/ a juste 1 chanson


        System.out.println("Chanson supprimee -> playlist mise à jour");
    }
    /** Complexite Temporelle: removeSong(int id) est ==>  O(n)
     * Pour rechercher l'element a supprimer il se peut qu'on parcours tout le tableau O(count) ceci est O(n)
     * Si l'element supprime est le premier ou le dernier on fait pas de decalage la complexite est O(1)
     * Decalage des elements dans le pire cas est O(n) si l'element est au debut du tableau
     * le reste des operation arithmetique(modulo , assignation ... ) ce fait en temps constant cad : O(1)
     */




    /**
     * Permet de lire les chansons dans un ordre aleatoire sans repetition en utilisant la fonction auxiliare
     * founie par l'enonce de l'exercice.
     */
    public void toggleShuffle() {
        //DONE
        if (count == 0) {
            System.out.println("La playlist est vide ajouter des chansons.");
            return;
        }
         // ce qui change l'etat de la variable boolean de faux a vrai et vice versa
        shuffleMode = !shuffleMode;

        // si vrai on prend un index aleatoire et on utilise la methode auxilaire playSong() pour faire la lecture.
        if (shuffleMode && count > 0) {
            System.out.println("Lecture en mode shuffle :");
            front = getRandomIndex();

        } else {
            System.out.println("Mode shuffle desactiver !");
        }
    }
      /**
        * Complexite Temporelle : toggleshuffle() ==> O(1)
        */


    /** la chanson en cours de lecture se rejoue en boucle */
    public void toggleRepeat() {
        //Done
        if (count == 0) {
            System.out.println("La playlist est vide ajouter des chansons.");
            return;
        }

        repeatMode = !repeatMode;   // Switch de true a false a chaque fois qu'on appel la methode (meme technique)

        // Si vrai on affiche le message playSong() gere le cas ou repeatMode est activer comme preciser a l'enonce
        if (repeatMode && count > 0) {
            System.out.println("Mode repeat activer !");

        } else {
            System.out.println("Mode repeat desactiver ! ");
            front = (front + 1) % maxSize;
        }
    }
    /**
     * Complexite Temporelle : toggleRepeat() ==> O(1)
     * Operation arithmetique simple
     */

        /**
         * Permet d'afficher toutes les chansons de la playlist avec leur details ( id, titre , Artiste , duree )
         */
            public void displayPlaylist() {
            //DONE
            if (count == 0) {
                System.out.println("La liste de chansons est vide ajouter un song svp.");

            }
            /*else if (count > 0) {
                int index = this.front; //pointeur debut<
                int fin = this.rear;
                while (true) { //meme chose que pr insertsort

                    if(playlist[index]==null){
                        index = (index + 1) % maxSize;
                    }

                    System.out.println("Lecture : " + playlist[index]); //je print
                    index = (index + 1) % maxSize; //on saute
                    if (index == fin ) {  //arrive a la fin -> arrete
                       System.out.println("Lecture : " + playlist[index]); // ca imprime 2 fois le premier song
                        break;

                    }

                }
            }*/
                int chansonJouee=0;
                int index = this.front; //pointeur debut<
                int fin = this.rear;
                while (chansonJouee < count) { // ON FAIT AVEC COUNT
                    if (playlist[index] != null) {  // JE filtre les null
                        System.out.println("Lecture : " + playlist[index]); //print
                        chansonJouee++; //next chanson
                    }
                    index = (index + 1) % maxSize; // On saute
                }
            }



    /** Complexite Temporelle : displayPlayliste() ==> O(n)
     * la premiere condition if(count == 0) s'execute en temps constant O(1)
     * la boucle while(chansonJouee < count ) s'execute pour un maximum de count fois
     * Donc O(n) , ou n le nombre de chansons dans la playlist
     * les autre verification if , print , mise a jour de index ce fait en temps constant O(1)
     */
    public static void main(String[] args) {
        Song song1 = new Song(1, "Chanson Un", "Artiste A", 120);
        Song song2 = new Song(2, "Chansons Deux", "Artiste B", 150);
        Song song3 = new Song(3, "Chansons trois ", "Artiste C", 180);
        Song song4 = new Song(4, "Chanson quatre", "Artiste D", 90);
        Song song5 = new Song(5, "Chanson cinq", "Artiste E", 200);
        Song song6 = new Song(6,"Chanson six","Artiste F",190 );

        CircularPlaylist playlist = new CircularPlaylist(5); // 5 est la capacité max

        try {
            System.out.println("\n== Test 1 : Ajout de chansons ==\n");

            playlist.addSong(song1);
            playlist.addSong(song2);
            playlist.addSong(song3);
            playlist.addSong(song4);
            playlist.addSong(song5);

        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("Erreur: " + e.getMessage());
        }

        System.out.println("\n== Test 2 : Affichage de la playlist initiale (displayPlaylist) ==\n");

        playlist.displayPlaylist();

        System.out.println("\n== Test 3 : Lecture en mode normal ==\n");

        try {
            Song song000 = playlist.playSong();
            Song song001 = playlist.playSong();

        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("Erreur: " + e.getMessage());
        }

        System.out.println("\n== Test 4 : Utilisation de nextSong() et previousSong() ==\n");

        try {
            System.out.println("Appel de nextSong() puis playSong():");
            playlist.nextSong();
            Song song002 = playlist.playSong();
            System.out.println("Appel de previousSong() puis playSong():");
            playlist.previousSong();
            Song song003 = playlist.playSong();

        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("Erreur: " + e.getMessage());
        }

        System.out.println("\n== Test 5 : Activation du mode repeat ==\n");

        try {
            playlist.toggleRepeat();
            Song song004 = playlist.playSong();
            Song song005 = playlist.playSong();

            playlist.toggleRepeat();
        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("Erreur: " + e.getMessage());
        }

        System.out.println("\n== Test 6 : Suppression d'une chanson (ID = 3) ==\n");

        try {

             playlist.removeSong(3);


            System.out.println("\nDisplay de toute la playliste après suppression en utilisant displayPlaylist():  \n");


        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        playlist.displayPlaylist();

        /**
         * Song song190 = playlist.playSong();
         * Song song917 = playlist.playSong();
         * Song song811 = playlist.playSong();
         * Song song862 = playlist.playSong();
         * Song song75 = playlist.playSong();
         **/

        System.out.println("\nNombre de chansons après suppression de la chanson ID = 3 est : " + playlist.count + "\n");

        try {
            System.out.println("== Test 7 : Activation du mode shuffle ==");
            System.out.println("\n");

            playlist.toggleShuffle();

            Song song006 = playlist.playSong();
            Song song007 = playlist.playSong();
            Song song008 = playlist.playSong();
            Song song009 = playlist.playSong();
            Song song010 = playlist.playSong();
            Song song030 = playlist.playSong();


            playlist.toggleShuffle();

        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("Erreur: " + e.getMessage());
        }

        System.out.println("\n== Test 8 : Lecture finale en mode normal ==\n");

        try {
            Song song011 = playlist.playSong();
            Song song012 = playlist.playSong();

        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("Erreur: " + e.getMessage());
        }

        System.out.println("\n== Test 9 : Test de rajout d'une autre chanson qui dépasse la taille du tableau. ==\n");

        try {
            playlist.addSong(song6);
            playlist.addSong(song3);

        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("Erreur: " + e.getMessage());
        }

    }
}



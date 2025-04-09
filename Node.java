package dv2;


public class Node {

    private int value;
    private Node next = null;

    /**
     * Constructeur avec valeur
     * @param value
     */
    public Node(int value) { //fait
        this.value = value;
        this.next = null;
    }

    /**
     * Constructeur avec valeur ET une node next en particulier
     * @param value
     * @param next
     */
    public Node(int value, Node next) { //fait
        this.next = next;
        this.value=value;
    }

    /**
     * Ajout d'une valeur (appel de constructeur par defaut) a la liste
     * @param value
     */
    public void addValue(int value) { //recursif, fait

        if(this.next == null){ //si jarrive a la fin (juste avant null)
            Node nouveau = new Node(value); //je garde l'ancien fin
            this.next = nouveau; //ajout
        }

        else {
            Node prochain = this.next; //je me deplace
            prochain.addValue(value); //appel recursif
        }
    }


    /**
     * Fct pour concatener  des nodes (ou liste si cette node next a un suivant) de facon récursive
     * en changeant le pointeur vers null de la premiere liste (sur laquelle on appelle cette methode) pour que son suivant
     * soit la node next
     * @param next
     */
    public void addNode(Node next) { //recursif,fait

        if (next==null){
            throw new IllegalArgumentException("on ajoute pas un node null");
        }
        //cas basique sans valeur la, avec constru par def

        if(this.next == null){
            this.next = next;
        }

        else {
            this.next.addNode(next);
        }
    }

    /**
     * Fct permettant de supprimer le dernier element recursivement, on le fait via manip des pointeurs
     * (on change le pointeur next de l'avant dernier pour pointer directement a null)
     */
    public void removeLast() {//recursif

        if(this.next == null){ //cas 1 SEUL
            throw new IllegalStateException("Pas possible de supprimer une liste d 1 element"); //PARCE QUE THIS NE PEUX PAS ETRE NULL
        }

        if (this.next.next == null) {
            this.next = null;
            return;
        }
     //else
            this.next.removeLast();

    }

    /**
     * Fct pour retirer toutes les nodes ayant comme valeur le param value
     * @param value, valeur des nodes a retirer
     */
    public void removeValue(int value) {// Recursif, code ayman

        // CAS DE DEBUT ou il est tout seul avec le next != null
        if ((this.value ==value)&&(this.next!=null)){ //la velaur est celle que je vx, et c'est pas le dernier ni le seul
            this.value=this.next.value;         //je le skip en pointant son prochain
            this.next=this.next.next;           //il aura le prochain de son prochain
            removeValue(value); //ON CONTINUE LA RÉCURSION
            return;                              //quand j'ai fini la recursion je retourne rien (vu le void
        }
        if(this.next  == null ){
            return;
        }
        if (this.next.value == value){
            this.next = this.next.next ;
            removeValue(value);
        }else{
            this.next.removeValue(value);} //appel recursif
    }

    /**
     * Fct qui traverse itérativement la liste er retourne le compteur de longueur
     * @return la longueur de la liste
     */
    public int length_iteratif() { //iteratif ,fait
        Node present = this;
        //if (present ==null){
          //  throw new NullPointerException("pas init");
            //return 0;
        //}
        int compteur=1;
        while(present.next != null){
             present = present.next;
             compteur++;
        }
        return compteur;
    }

    /**
     * Fct récursive qui retourne la longueur en parcourant récursivement jusqua null
     * @return la longueur de la liste
     */
    public int length_recurssion() { //recursif
        if (this.next==null){ //si le prochain = null
            return 1; //on compte le node quon vient de visiter comme "+1"
        }
        else {
           return 1 + this.next.length_recurssion(); //sinon, on continue de compter en additionnant 1 a la valeur quon trouvera dans le prochain appel (pour next)
        }
    }


    /**
     * Fct permettant de retourner le n ieme element en partant de null
     *
     * @param nLast, le n ieme element a partir de la fin
     * @return
     */
    public int returnNlast(int nLast) { //ITERATIF

        /*if (this == null){
            throw new IllegalStateException("liste VIDE!");
        }*/

        if(nLast < 0){
            System.out.println("nLast doit etre non negatif!");
            return -1; //J'aurais mi un throw Exception mais dans le doc on demande de return -1
        }

        Node present=this;
        int longueur = present.length_iteratif(); //juste plus simple que recursif
        //Le 20 ieme element c'est' size - 20 element

        if (nLast > longueur){ //cas impossible
            System.out.println("nLast doit pas depasser les index possibles de la liste");
            return -1;
        }
        int nodeCherchee = (longueur - nLast); //cas impossible
        if (nodeCherchee < 0) {
            System.out.println("on depasse la liste");
        }

        for (int i=0; i<nodeCherchee; i++) {
            present=present.next;
        }
        return present.value;
    }


    /**
     * Fct permettant de construire et ajouter en ordre des nodes pour une liste auxiliaire au tri par insertSORT
     * @param value la valeur de la node a ajouter
     */
    public void addValue_ordered(int value) {
        if (this.next == null) {//si on es la fin -> on ajoute directement
            this.next = new Node(value);
        }
        else if (this.next.value > value) { // si non si le next est superieur ajouter avant
            this.next = new Node(value, this.next);
        }
        else {
            this.next.addValue_ordered(value); //sinon on continue de trouver un next plus grand recurse
        }
    }


    /**
     * Fct de tri par insertion, va parcourir du début jusqua la fin en trouvant le premier minimum, puis
     * le deuxieme, le troisieme jusqua finir avec le dernier min (essentiellement le max de la liste )
     */
    public void insertSort() { //
        // c'est comme le bublbe sort mais on place les éléments en premier dans une nouvelle liste vide

        Node present; //la node quon a au debut
        Node pntMin; //pointeur du min trouvee
        Node prevmin; //le precedent du min
        Node prev; //le precedent de l iteraton ds la boucle
        Node listetriee = null; //la list triee

        // verif cas si vide OU si c'Est juste une liste de 1
        if (this == null || this.next == null) {
            return; //rien a faire la, pas changement
        }

        while (true) { //tant quon a pas decide darreter de trier

            present = this; //on update present avec this (la node sur laquelle on a call la fct
            prev = null;
            pntMin = this;
            prevmin = null;

                while (present != null) { //boucle interne pour comparer le min aux autre potentiels min
                    if (present.value < pntMin.value) { //si on en trouve 1
                        pntMin = present;//on update
                        prevmin = prev; //on update aussi le precedent du min
                    }
                    prev = present; //no passe au prochain dans tt les cas
                    present = present.next;
                }

            //la, on a fait le tour de boucle, on a un min garanti trouvee par comparaison ITERATIVES
            if (listetriee == null) { //cas SI LA LISTE ON LUI A RIEN MIT
                listetriee = new Node(pntMin.value); //on cree a liste avec cette valeur min quon a trouve
            } else {
                listetriee.addValue_ordered(pntMin.value); //SINON PAS VIDE -> on call add value ordered
            }

            //  POUR ARRETER DE CHERCHER UN MIN
            if (prevmin == null && this.next == null) { //SI on a un prevmin qui est null  (le min est le premier element) OU qu'on a aucun suivant
                break; // on a fini de trier
            }

            if (pntMin == this) { // Si pntMin est au DEBUT ET ON A PAS FINI
                this.value = this.next.value; //Lobjet node quon traitait au tout debut (ref inchangeable) sa valeur devient celle de la prochaine
                this.next = this.next.next; //on update son prochain pour son "prochain"
            } else if (prevmin != null) {
                prevmin.next = pntMin.next; //on va techniquement supprimer le min trouvé en "l ignorant", le next de son previous,c'est le next du min
            }
        }
        if (listetriee != null) {
            this.value = listetriee.value; //la ref de la liste techniquement cest = le premier node de this, on va juste dire que maintenant c'Est le node du tab triee
            this.next = listetriee.next;
        }
    }

    /**
     * Fct ajoutee/facultative, sert a afficher la liste
     */
    public void affichage(){
        Node test = this; //p0ur garder la ref Intacte

        while (test != null) {
            System.out.print(test.value + " ");
            test = test.next;
        }
        System.out.println();

    }


    public static void main(String[] args) {

        System.out.print("test 1, creation de la liste: ");

        Node node = new Node(5);
        node.addValue(1);
        node.addValue(4);
        node.addValue(5);
        node.addValue(5);
        node.addValue(5);
        node.addValue(5);
        node.addValue(3);
        node.addValue(9);
        node.affichage();

        System.out.print("test 2a, longueur de la liste (iteratif) : ");
        System.out.println(node.length_iteratif());
        System.out.print("test 2b, longueur de la liste (recursif) : ");
        System.out.println(node.length_recurssion());


        System.out.print("test 3a, returnNlast 1 : ");
        System.out.println(node.returnNlast(1));
        System.out.print("test 3b, returnNlast 9 : ");
        System.out.println(node.returnNlast(9));
        System.out.print("test 3c, returnNlast -1 : ");
        System.out.println(node.returnNlast(-1));
        System.out.print("test 3d, returnNlast 12 : ");
        System.out.println(node.returnNlast(12));


        System.out.print("test 4a, removeLast : ");
        node.removeLast();
        node.affichage();

        try{
        System.out.print("test 4b, removeLast : ");
        Node vide = new Node(5);
        vide.removeLast();

        }catch(IllegalStateException e){
            System.out.println("ERREUR : " +e.getMessage() );
        }


        System.out.print("test 5, removeValue 5 : ");
        node.removeValue(5);
        node.affichage();

        System.out.print("test 6a, addNode: ");
        //on va concatener une autre liste node2
        Node node2 = new Node(18);
        node2.addValue(11);
        node2.addValue(10);
        node2.addValue(11);
        node2.addValue(2);
        node2.addValue(5);
        node.addNode(node2);
        node.affichage();


        try{
            System.out.print("test 6b, addNode: ");
        node.addNode(null);
        }
        catch(IllegalArgumentException e){
            System.out.println("ERREUR : " +e.getMessage() );
        }

        System.out.print("test 7a, addvalue_ordered(9) : ");
        node.addValue_ordered(9);
        node.affichage();

        System.out.print("test 7b, insertSort : ");
        node.insertSort();
        node.affichage();



    }
}


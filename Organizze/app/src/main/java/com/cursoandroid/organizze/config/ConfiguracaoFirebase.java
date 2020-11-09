package com.cursoandroid.organizze.config;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {
    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebase;

    //retorna a intancida do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){
        autenticacao = FirebaseAuth.getInstance();

        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    //retorna a intancia do Firebase
    public static DatabaseReference getFirebaseDatabase(){
        if(firebase == null){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }
}
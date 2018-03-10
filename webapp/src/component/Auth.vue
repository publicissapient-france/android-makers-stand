<template>
  <div class="auth">
    <h1>Simon Game by Xebia</h1>
    <p>Only a <span>Xebian</span> can access this resource,
      <br/>please use your <code>@xebia.fr</code> address.</p>
    <div class="auth-container"></div>
  </div>
</template>

<script>
  import FirebaseUi from 'firebaseui';
  import Firebase from '../firebase';

  export default {
    name: 'auth',
    mounted() {
      new FirebaseUi.auth.AuthUI(Firebase.auth())
        .start('.auth-container', {
          signInSuccessUrl: '/',
          signInOptions: [
            Firebase.auth.GoogleAuthProvider.PROVIDER_ID,
          ],
        });
    },
  };
</script>

<style scoped>

</style>

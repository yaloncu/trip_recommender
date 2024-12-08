import { signInWithPopup } from 'firebase/auth';
import { auth, googleProvider } from '@/firebaseConfig';

const signInWithGoogle = async () => {
  try {
    const result = await signInWithPopup(auth, googleProvider);
    const user = result.user;
    console.log('Usuario autenticado:', user);
    return user;
  } catch (error) {
    console.error('Error al iniciar sesión con Google:', error.message);
    throw error;
  }
};

export { signInWithGoogle };

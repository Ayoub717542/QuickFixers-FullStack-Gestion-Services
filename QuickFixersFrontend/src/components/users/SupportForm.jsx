import { useForm } from "react-hook-form";
import { toast } from "react-toastify";
import { axiosApi } from "../../api/axiosApi";

function SupportForm({ onSuccess }) {
    const {
        register,
        handleSubmit,
        reset,
        formState: {errors, isSubmitting}
    } = useForm();

    function onSubmit(data) {
        axiosApi.post("/users/Ajoutersupport", data)
            .then(() => {
                toast.success("Support ajouté avec succès.");
                reset();
                onSuccess();
            })
            .catch(() => {
                toast.error("Erreur lors de l'ajout du support.");
            });
    }
}
export default SupportForm;
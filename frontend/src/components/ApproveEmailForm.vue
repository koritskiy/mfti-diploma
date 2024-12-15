<template>
    <form @submit.prevent method="post">
        <text-input
                label="Код из почты"
                placeholder="Например, 123456"
                class="mt-3"
                v-model:model-value="secretCode"
        />

        <div class="flex justify-center">
            <success-outline-button @click="approveEmail" class="">Подтвердить почту</success-outline-button>
        </div>
    </form>
</template>
<script>
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";
import TextInput from "@/components/ui/TextInput.vue";

export default {
    components: {TextInput, SuccessOutlineButton},
    data() {
        return {
            secretCode: ''
        }
    },
    methods: {
        approveEmail() {
            this.$emit('create', this.secretCode)
        }
    }
}
</script>
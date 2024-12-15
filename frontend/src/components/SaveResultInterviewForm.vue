<template>
    <form @submit.prevent method="post">
        <textarea-element
            placeholder="Расскажите свое мнение о кандидате"
            label="Ваш отзыв"
            v-model:model-value="interview.interviewFeedback"
        />

        <textarea-element
            placeholder="Например, точно надо брать"
            label="Расскажите, какое решение Вы хотите принять"
            v-model:model-value="interview.interviewDecision"
        />

        <div class="flex justify-center">
            <success-outline-button @click="createResultInterview" class="">Сохранить результат</success-outline-button>
        </div>
    </form>
</template>

<script>
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";
import TextareaElement from "@/components/ui/TextareaElement.vue";

export default {
    components: {TextareaElement, SuccessOutlineButton},
    data() {
        return {
            interview: {
                interviewFeedback: '',
                interviewDecision: ''
            }
        }
    },
    methods: {
        createResultInterview() {
            this.$emit('create', this.interview)
        }
    }
}
</script>
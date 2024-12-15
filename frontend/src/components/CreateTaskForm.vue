<template>
    <form @submit.prevent method="post">
        <text-input
                label="Название задачи"
                v-model:model-value="task.name"
        />

        <text-input
                label="Описание"
                v-model:model-value="task.description"
        />

        <div class="flex justify-center">
            <success-outline-button @click="createTask" class="">Создать</success-outline-button>
        </div>
    </form>
</template>

<script>
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";
import TextInput from "@/components/ui/TextInput.vue";

export default {
    components: {SuccessOutlineButton, TextInput},
    props: {
        boardColumnId: {
            type: Number,
            required: true
        }
    },
    data() {
        return {
            task: {
                name: '',
                description: '',
                boardColumnId: this.boardColumnId
            }
        }
    },
    methods: {
        createTask() {
            this.$emit('create', this.task)
        }
    }
}
</script>
<template>
    <form @submit.prevent method="post">
        <text-input
                label="Название колонки"
                v-model:model-value="column.name"
        />

        <div class="flex justify-center">
            <success-outline-button @click="createColumn" class="">Создать</success-outline-button>
        </div>
    </form>
</template>
<script>
import TextInput from "@/components/ui/TextInput.vue";
import SuccessOutlineButton from "@/components/ui/SuccessOutlineButton.vue";

export default {
    components: {TextInput, SuccessOutlineButton},
    props: {
        boardId: {
            type: Number,
            required: true
        }
    },
    data() {
        return {
            column: {
                name: '',
                boardId: this.boardId
            }
        }
    },
    methods: {
        createColumn() {
            this.$emit('create', this.column)
        }
    }
}

</script>
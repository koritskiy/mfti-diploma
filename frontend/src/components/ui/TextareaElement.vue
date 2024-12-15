<template>
    <div class="form-control">
        <label class="label">
            <span class="label-text">{{ label }}</span>
        </label>
        <textarea
            :value="modelValue"
            @input="updateTextarea"
            class="textarea textarea-bordered h-24"
            :placeholder="placeholder"
        ></textarea>
    </div>
</template>
<script>
export default {
    name: 'textarea-element',
    props: {
        label: {
            type: String,
            required: false
        },
        placeholder: {
            type: String,
            required: false
        },
        modelValue: [String]
    },
    methods: {
        updateTextarea(event) {
            this.$emit('update:modelValue', event.target.value)
        }
    }
}
</script>
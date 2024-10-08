/**
 * @license Copyright (c) 2003-2023, CKSource Holding sp. z o.o. All rights reserved.
 * For licensing, see LICENSE.md or https://ckeditor.com/legal/ckeditor-oss-license
 */
// @ts-ignore
import { ClassicEditor as ClassicEditorBase } from '@ckeditor/ckeditor5-editor-classic';
// @ts-ignore
import { Essentials } from '@ckeditor/ckeditor5-essentials';
// @ts-ignore
import { UploadAdapter } from '@ckeditor/ckeditor5-adapter-ckfinder';
// @ts-ignore
import { Autoformat } from '@ckeditor/ckeditor5-autoformat';
// @ts-ignore
import { Bold, Italic } from '@ckeditor/ckeditor5-basic-styles';
// @ts-ignore
import { BlockQuote } from '@ckeditor/ckeditor5-block-quote';
// @ts-ignore
import { CKBox } from '@ckeditor/ckeditor5-ckbox';
// @ts-ignore
import { CKFinder } from '@ckeditor/ckeditor5-ckfinder';
// @ts-ignore
import { EasyImage } from '@ckeditor/ckeditor5-easy-image';
// @ts-ignore
import { Heading } from '@ckeditor/ckeditor5-heading';
// @ts-ignore
import { Image, ImageCaption, ImageStyle, ImageToolbar, ImageUpload, PictureEditing } from '@ckeditor/ckeditor5-image';
// @ts-ignore
import { Indent } from '@ckeditor/ckeditor5-indent';
// @ts-ignore
import { Link } from '@ckeditor/ckeditor5-link';
// @ts-ignore
import { List } from '@ckeditor/ckeditor5-list';
// @ts-ignore
import { MediaEmbed } from '@ckeditor/ckeditor5-media-embed';
// @ts-ignore
import { Paragraph } from '@ckeditor/ckeditor5-paragraph';
// @ts-ignore
import { PasteFromOffice } from '@ckeditor/ckeditor5-paste-from-office';
// @ts-ignore
import { Table, TableToolbar } from '@ckeditor/ckeditor5-table';
// @ts-ignore
import { TextTransformation } from '@ckeditor/ckeditor5-typing';
// @ts-ignore
import { CloudServices } from '@ckeditor/ckeditor5-cloud-services';
export default class ClassicEditor extends ClassicEditorBase {
    static builtinPlugins: (typeof TextTransformation | typeof Essentials | typeof UploadAdapter | typeof Paragraph | typeof Heading | typeof Autoformat | typeof Bold | typeof Italic | typeof BlockQuote | typeof Image | typeof ImageCaption | typeof ImageStyle | typeof ImageToolbar | typeof ImageUpload | typeof CloudServices | typeof CKBox | typeof CKFinder | typeof EasyImage | typeof List | typeof Indent | typeof Link | typeof MediaEmbed | typeof PasteFromOffice | typeof Table | typeof TableToolbar | typeof PictureEditing)[];
    static defaultConfig: {
        toolbar: {
            items: string[];
        };
        image: {
            toolbar: string[];
        };
        table: {
            contentToolbar: string[];
        };
        language: string;
    };
}
